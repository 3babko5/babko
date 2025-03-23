package com.business.order.application.service;

import com.business.common.application.exception.BusinessLogicException;
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
import com.business.order.infrastructure.dto.response.ProductDetailResponseDto;
import com.business.order.infrastructure.dto.response.hubIdResponseDto;
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
        List<OrderItemRequestDto> items = request.getItems();

        for (OrderItemRequestDto item : items) {

            ProductDetailResponseDto queryDto = new ProductDetailResponseDto(
                    item.getProductId(),
                    null, // 가격은 요청에는 필요 없음
                    null, // 재고도 필요 없음
                    null  // 공급자도 필요 없음
            );

            ProductDetailResponseDto productDetail = productFeignClient.getProductDetail(queryDto);

            if(item.getOrderItemAmount() > productDetail.getProductQuantity()){
                throw new BusinessLogicException(OrderExceptionCode.PRODUCT_QUANTITY_EXCEEDED);
            }

            item.setSupplierId(productDetail.getSupplierId());
            item.setOrderItemPrice(productDetail.getProductPrice()); //여기서 매핑
        }

        UUID supplierId = items.get(0).getSupplierId();//첫번째 주문 아이템을 꺼내서 공급id 변수에 저장
        UUID receiverId = request.getReceiverId();

        hubIdResponseDto supplierResponse = companyFeignClient.searchCompanies(CompanyType.SUPPLIER);
        UUID originHubId = extractHubIdFromCompanyResponse(supplierResponse, supplierId);

        hubIdResponseDto receiverResponse = companyFeignClient.searchCompanies(CompanyType.RECEIVER);
        UUID destinationHubId = extractHubIdFromCompanyResponse(receiverResponse, receiverId);
        Order order = request.createOrder(userId, originHubId, destinationHubId);

        List<OrderItem> orderItems = items.stream()
                .map(i -> i.createOrderItem(order))
                .collect(Collectors.toList());

        order.addOrderItems(orderItems);
        Order savedOrder  = orderRepository.save(order);

        OrderDeliveryRequestDto deliveryRequest = OrderDeliveryRequestDto.fromOrder(savedOrder);
        if(!deliveryFeignClient.createDelivery(deliveryRequest)){
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
        log.info("3. Cancel order {}", orderId);
        return OrderMapper.toOrderStatusResponseDto(order);
    }

    private UUID extractHubIdFromCompanyResponse(hubIdResponseDto response, UUID targetCompanyId) {
        return response.getCompany().stream()
                .filter(c -> c.getCompanyId().equals(targetCompanyId))
                .findFirst()
                .map(hubIdResponseDto.CompanyData::getHubId)
                .orElseThrow(() -> new BusinessLogicException(OrderExceptionCode.COMPANY_NOT_FOUND));
    }
}
