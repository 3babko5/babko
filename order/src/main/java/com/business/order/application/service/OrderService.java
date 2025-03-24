package com.business.order.application.service;

import com.business.common.application.exception.BusinessLogicException;
import com.business.order.application.dto.mapper.RequestMapper;
import com.business.order.application.dto.request.OrderCreateRequestDto;
import com.business.order.application.dto.request.OrderItemRequestDto;
import com.business.order.application.dto.response.OrderStatusResponseDto;
import com.business.order.application.dto.response.OrderCreateResponseDto;
import com.business.order.application.dto.mapper.OrderMapper;
import com.business.order.application.dto.response.OrderGetResponseDto;
import com.business.order.application.exception.OrderExceptionCode;
import com.business.order.domain.entity.CompanyType;
import com.business.order.domain.entity.Order;
import com.business.order.domain.entity.OrderItem;
import com.business.order.domain.repository.OrderRepository;
import com.business.order.infrastructure.client.CompanyFeignClient;
import com.business.order.infrastructure.client.DeliveryFeignClient;
import com.business.order.infrastructure.client.ProductFeignClient;
import com.business.order.infrastructure.dto.request.OrderDeliveryRequestDto;
import com.business.order.application.dto.response.ProductDetailResponseDto;
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
        //클라이언트가 보낸 주문 요청에서 아이템 목록을 꺼냄
        List<OrderItemRequestDto> items = request.getItems();

        //각 아이템에 대해 반복처리 시작
        for (OrderItemRequestDto item : items) {
            //상품id 추출
            UUID productId = item.getProductId();

            // 상품 검색 API 호출 - response는 상품 검색 결과 전체를 담고 있는 DTO
            GetProductInfoResponseDto response = productFeignClient.searchProduct(
                    productId, null, null, 0, 1000, "productName", "asc"
            );

            //GetProductInfoResponseDto 안에 있는 List<ProductData> 형태
            List<GetProductInfoResponseDto.ProductData> products = response.getProduct();//"product" 라는 리스트에 들어있는 모든 상품 객체

            //상품id에서 원하는 값 추출해서 dto에 저장????
            GetProductInfoResponseDto.ProductData targetProduct = products.stream()
                    .filter(p -> p.getProductId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new BusinessLogicException(OrderExceptionCode.PRODUCT_NOT_FOUND));

            // 필요한 필드만 추출해 커스텀 DTO로 매핑
            ProductDetailResponseDto productDetail = RequestMapper.toProductDetailResponse(targetProduct);

            //재고확인 로직
            if(item.getOrderItemAmount() > productDetail.getProductQuantity()){
                throw new BusinessLogicException(OrderExceptionCode.PRODUCT_QUANTITY_EXCEEDED);
            }

            // OrderItemRequestDto 안의 supplierId 필드를 채워주는 부분 (이 전엔 null값이었음)
            item.setSupplierId(productDetail.getSupplierId());//상품api에서 조회한 공급자id를 주문아이템에 저장
            item.setOrderItemPrice(productDetail.getOrderItemPrice());
        }

        UUID supplierId = items.get(0).getSupplierId();//첫번째 주문 아이템을 꺼내서 공급id 변수에 저장
        UUID receiverId = request.getReceiverId();

        UUID originHubId = extractHubIdByCompanyId(CompanyType.SUPPLIER, supplierId);
        UUID destinationHubId = extractHubIdByCompanyId(CompanyType.RECEIVER, receiverId);

        Order order = request.createOrder(userId, originHubId, destinationHubId);

        List<OrderItem> orderItems = items.stream()
                .map(i -> i.createOrderItem(order))
                .collect(Collectors.toList());

        order.addOrderItems(orderItems);
        Order savedOrder  = orderRepository.save(order);

        OrderDeliveryRequestDto deliveryRequest = OrderDeliveryRequestDto.fromOrder(savedOrder);

        try {
            deliveryFeignClient.createDelivery(deliveryRequest);
            // 성공 시 추가 로직 (필요하면)
        } catch (FeignException e) {
            throw new BusinessLogicException(OrderExceptionCode.DELIVERY_REQUEST_FAILED);
        }

        return OrderMapper.toOrderCreateResponseDto(savedOrder, orderItems);
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

    private UUID extractHubIdByCompanyId(CompanyType type, UUID companyId) {
        GetCompanyInfoResponseDto response = companyFeignClient.searchCompanies(
                null, type, 0, 1000, "createdAt", "asc"
        );

        return response.getCompany().stream()
                .filter(c -> c.getCompanyId().equals(companyId))
                .findFirst()
                .map(GetCompanyInfoResponseDto.CompanyData::getHubId)
                .orElseThrow(() -> new BusinessLogicException(OrderExceptionCode.COMPANY_NOT_FOUND));
    }

}
