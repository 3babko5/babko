package com.business.order.application.service;

import com.business.common.application.exception.BusinessLogicException;
import com.business.order.application.dto.mapper.DeliveryMapper;
import com.business.order.application.dto.mapper.RequestMapper;
import com.business.order.application.dto.request.OrderCreateRequestDto;
import com.business.order.application.dto.request.OrderItemRequestDto;
import com.business.order.application.dto.response.*;
import com.business.order.application.dto.mapper.OrderMapper;
import com.business.order.application.exception.OrderExceptionCode;
import com.business.order.domain.entity.CompanyType;
import com.business.order.domain.entity.Order;
import com.business.order.domain.entity.OrderItem;
import com.business.order.domain.repository.OrderRepository;
import com.business.order.infrastructure.client.CompanyFeignClient;
import com.business.order.infrastructure.client.DeliveryFeignClient;
import com.business.order.infrastructure.client.ProductFeignClient;
import com.business.order.infrastructure.dto.queryDto.SearchCompanyQueryDto;
import com.business.order.infrastructure.dto.queryDto.SearchProductQueryDto;
import com.business.order.infrastructure.dto.request.CreateDeliveryRequestDto;
import com.business.order.infrastructure.dto.request.OrderDeliveryRequestDto;
import com.business.order.infrastructure.dto.response.GetCompanyInfoResponseDto;
import com.business.order.infrastructure.dto.response.GetDeliveryCreateResponseDto;
import com.business.order.infrastructure.dto.response.GetProductInfoResponseDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.business.common.infrastructure.util.JpaUtil.OrderBy.CREATED;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DeliveryFeignClient deliveryFeignClient;
    private final ProductFeignClient productFeignClient;
    private final CompanyFeignClient companyFeignClient;

    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto request, Long userId){
        log.info("주문 생성 요청 시작 - userId: {}, request: {}", userId, request);

        List<OrderItemRequestDto> items = request.getItems();

        for (OrderItemRequestDto item : items) {
            UUID productId = item.getProductId();
            log.info("상품 조회 시작 - productId: {}", productId);

            SearchProductQueryDto queryDto = new SearchProductQueryDto(
                    productId, null, null, null,
                    1, 1000, "CREATED", "asc"
            );

            GetProductInfoResponseDto response;
            try {
                response = productFeignClient.searchProduct(queryDto);
                log.info("상품 조회 성공 - 응답 데이터: {}", response);
            } catch (FeignException e) {
                throw new BusinessLogicException(OrderExceptionCode.PRODUCT_NOT_FOUND);
            }

            List<GetProductInfoResponseDto.ProductData> products = response.getProduct();
            log.info("조회된 상품 수: {}", products.size());

            GetProductInfoResponseDto.ProductData targetProduct = products.stream()
                    .filter(p -> p.getProductId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> {
                        log.error("해당 productId의 상품을 찾을 수 없음: {}", productId);
                        return new BusinessLogicException(OrderExceptionCode.PRODUCT_NOT_FOUND);
                    });

            ProductDetailResponseDto productDetail = RequestMapper.toProductDetailResponse(targetProduct);
            log.info("상품 상세 정보: {}", productDetail);

            if(item.getOrderItemAmount() > productDetail.getProductQuantity()){
                throw new BusinessLogicException(OrderExceptionCode.PRODUCT_QUANTITY_EXCEEDED);
            }

            item.setSupplierId(productDetail.getSupplierId());
            item.setOrderItemPrice(productDetail.getOrderItemPrice());
            log.info("공급자 ID, 가격 설정 완료 - supplierId: {}, price: {}",
                    item.getSupplierId(), item.getOrderItemPrice());
        }

        UUID supplierId = items.get(0).getSupplierId();
        UUID receiverId = request.getReceiverId();
        log.info("추출된 공급자 ID: {}", supplierId);
        log.info("추출된 수령자 ID: {}", receiverId);

        UUID originHubId = extractHubIdByCompanyId(CompanyType.SUPPLIER, supplierId);
        UUID destinationHubId = extractHubIdByCompanyId(CompanyType.RECEIVER, receiverId);

        Order order = request.createOrder(userId, originHubId, destinationHubId);

        List<OrderItem> orderItems = items.stream()
                .map(i -> i.createOrderItem(order))
                .collect(Collectors.toList());
        order.addOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        OrderDeliveryRequestDto deliveryRequest = OrderDeliveryRequestDto.fromOrder(savedOrder);

        CreateDeliveryRequestDto deliveryFeignRequest = DeliveryMapper.toCreateDeliveryRequestDto(deliveryRequest);

        log.info("배송 요청 생성 - Feign에 전달될 최종 값: {}", deliveryFeignRequest);

        try {
            deliveryFeignClient.createDelivery(deliveryFeignRequest);
            log.info("배송 요청 성공 - orderId: {}", savedOrder.getOrderId());
        } catch (FeignException e) {
            log.error("배송 요청 실패 - orderId: {}, error: {}", savedOrder.getOrderId(), e.getMessage(), e);
            throw new BusinessLogicException(OrderExceptionCode.DELIVERY_REQUEST_FAILED);
        }

        OrderCreateResponseDto responseDto = OrderMapper.toOrderCreateResponseDto(savedOrder, orderItems);
        log.info("주문 생성 완료 - responseDto: {}", responseDto);

        return responseDto;
    }


    @Transactional(readOnly = true)
    public OrderGetResponseDto getOrder(UUID orderId) {

        Order order = orderRepository.findByOrderIdWithItems(orderId);

        if (order == null) {
            throw new BusinessLogicException(OrderExceptionCode.ORDER_NOT_FOUND);
        }

        return OrderMapper.toOrderGetResponse(order);
    }

    @Transactional
    public OrderStatusResponseDto cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessLogicException(OrderExceptionCode.ORDER_NOT_FOUND));

        String deliveryStatus = deliveryFeignClient.getDeliveryStatus(orderId);

        if(!deliveryStatus.equals("WAITING_AT_HUB")){
            throw new BusinessLogicException(OrderExceptionCode.ORDER_CANNOT_BE_CANCELED);
        }

        order.cancelOrder(order.getUserId());
        orderRepository.save(order);
        return OrderMapper.toOrderStatusResponseDto(order);
    }

    private UUID extractHubIdByCompanyId(CompanyType type, UUID targetCompanyId) {
        SearchCompanyQueryDto queryDto = new SearchCompanyQueryDto();
        queryDto.setCompanyType(type);
        queryDto.setPage(1);
        queryDto.setSize(1000);
        queryDto.setOrderBy("CREATED");
        queryDto.setSort("asc");

        // 2. 업체 조회 API 호출 → 원본 응답 DTO
        log.info("companyFeignClient 조회 시작 - companyType: {}", type);
        GetCompanyInfoResponseDto rawResponse = companyFeignClient.searchCompanies(queryDto);

        // 3. 필요한 값만 담은 커스텀 DTO로 변환
        hubIdResponseDto simplified = RequestMapper.toHubIdResponse(rawResponse);

        // 4. 리스트 꺼냄 (company 안에 있는 실제 데이터 리스트)
        List<hubIdResponseDto.CompanyData> companyList = simplified.getCompany();

        log.info("조회된 업체 수: {}", companyList.size());
        companyList.forEach(c -> log.info("업체 ID: {}, 타입: {}, 허브ID: {}", c.getCompanyId(), c.getCompanyType(), c.getHubId()));

        // 5. 타입에 따라 필터링 및 허브 ID 추출
        return companyList.stream()
                .filter(c -> {
                    if (type == CompanyType.SUPPLIER) {
                        return c.getSupplierId().equals(targetCompanyId);
                    } else {
                        return c.getReceiverId().equals(targetCompanyId);
                    }
                })
                .findFirst()
                .map(hubIdResponseDto.CompanyData::getHubId)
                .orElseThrow(() -> new BusinessLogicException(OrderExceptionCode.COMPANY_NOT_FOUND));
    }


}
