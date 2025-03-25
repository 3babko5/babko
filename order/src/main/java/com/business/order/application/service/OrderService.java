package com.business.order.application.service;

import com.business.common.application.exception.BusinessLogicException;
import com.business.common.application.exception.GlobalExceptionCode;
import com.business.common.application.exception.GlobalExceptionHandler;
import com.business.order.application.dto.mapper.DeliveryMapper;
import com.business.order.application.dto.mapper.OrderMapper;
import com.business.order.application.dto.mapper.RequestMapper;
import com.business.order.application.dto.request.OrderCreateRequestDto;
import com.business.order.application.dto.request.OrderItemRequestDto;
import com.business.order.application.dto.request.SearchOrderRequestDto;
import com.business.order.application.dto.response.*;
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
import com.business.order.infrastructure.dto.request.DeliverySearchForOrderRequestDto;
import com.business.order.infrastructure.dto.request.OrderDeliveryRequestDto;
import com.business.order.infrastructure.dto.response.DeliveryListForOrderResponseDto;
import com.business.order.infrastructure.dto.response.GetCompanyInfoResponseDto;
import com.business.order.infrastructure.dto.response.GetProductInfoResponseDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

            SearchProductQueryDto queryDto = new SearchProductQueryDto(
                    productId, null, null, null,
                    1, 1000, "CREATED", "asc"
            );

            GetProductInfoResponseDto response;
            try {
                response = productFeignClient.searchProduct(queryDto);
            } catch (FeignException e) {
                throw new BusinessLogicException(OrderExceptionCode.PRODUCT_NOT_FOUND);
            }

            List<GetProductInfoResponseDto.ProductData> products = response.getProduct();

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
        }

        UUID supplierId = items.get(0).getSupplierId();
        UUID receiverId = request.getReceiverId();

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
        } catch (FeignException e) {
            throw new BusinessLogicException(OrderExceptionCode.DELIVERY_REQUEST_FAILED);
        }

        OrderCreateResponseDto responseDto = OrderMapper.toOrderCreateResponseDto(savedOrder, orderItems);

        return responseDto;
    }


    @Transactional(readOnly = true)
    public OrderGetResponseDto getOrder(UUID orderId, Long userId, String role)  {

        Order order = orderRepository.findByOrderIdWithItems(orderId);

        if (order == null) {
            throw new BusinessLogicException(OrderExceptionCode.ORDER_NOT_FOUND);
        }

        if ("ROLE_COMPANY".equals(role) && !order.getUserId().equals(userId)) {
            throw new BusinessLogicException(GlobalExceptionCode.FORBIDDEN);
        }

        return OrderMapper.toOrderGetResponse(order);
    }

    @Transactional(readOnly = true)
    public SearchOrderResponseDto searchOrders(SearchOrderRequestDto request, Pageable pageable, Long userId, String role)  {
        if ("ROLE_COMPANY".equals(role)) {
            request.setUserId(userId); // 본인 주문만 검색
        }

        Page<Order> orderPage = orderRepository.search(
                request.getOrderId(),
                request.getOrderStatus(),
                pageable
        );
        return OrderMapper.toSearchOrderResponseDto(orderPage);
    }

    //주문 취소
    @Transactional
    public OrderStatusResponseDto cancelOrder(UUID orderId, Long userId, String role) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessLogicException(OrderExceptionCode.ORDER_NOT_FOUND));

        if ("ROLE_COMPANY".equals(role) && !order.getUserId().equals(userId)) {
            throw new BusinessLogicException(GlobalExceptionCode.FORBIDDEN);
        }

        DeliverySearchForOrderRequestDto request = DeliverySearchForOrderRequestDto.fromOrderId(orderId);
        log.info("배송 상태 조회 요청 DTO 생성: {}", request);

        DeliveryListForOrderResponseDto<DeliveryStatusForOrderDto> response =
                deliveryFeignClient.getDeliveries(request);
        log.info("배송 서비스 응답 수신: {}", response);

        if (response.getData().isEmpty()) {
            throw new BusinessLogicException(OrderExceptionCode.DELIVERY_ID_NOT_FOUND);
        }

        DeliveryStatusForOrderDto deliveryInfo = response.getData().get(0);
        String deliveryStatus = deliveryInfo.getDeliveryStatus();

        if (!"WAITING_AT_HUB".equals(deliveryStatus)) {
            throw new BusinessLogicException(OrderExceptionCode.ORDER_CANNOT_BE_CANCELED);
        }

        order.cancelOrder(order.getUserId());

        // 배송 상태도 취소로 변경 요청
        try {
            deliveryFeignClient.cancelDelivery(deliveryInfo.getDeliveryId());
        } catch (FeignException e) {
            throw new BusinessLogicException(OrderExceptionCode.DELIVERY_STATUS_NOT_FOUND);
        }

        return OrderMapper.toOrderStatusResponseDto(order);
    }

    private UUID extractHubIdByCompanyId(CompanyType type, UUID targetCompanyId) {
        SearchCompanyQueryDto queryDto = new SearchCompanyQueryDto();
        queryDto.setCompanyType(type);
        queryDto.setPage(1);
        queryDto.setSize(1000);
        queryDto.setOrderBy("CREATED");
        queryDto.setSort("asc");

        GetCompanyInfoResponseDto rawResponse = companyFeignClient.searchCompanies(queryDto);

        hubIdResponseDto simplified = RequestMapper.toHubIdResponse(rawResponse);

        List<hubIdResponseDto.CompanyData> companyList = simplified.getCompany();

        companyList.forEach(c -> log.info("업체 ID: {}, 타입: {}, 허브ID: {}", c.getCompanyId(), c.getCompanyType(), c.getHubId()));

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
    //주문 완료

    @Transactional
    public void completeOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessLogicException(OrderExceptionCode.ORDER_NOT_FOUND));

        order.completeOrder();
    }
}
