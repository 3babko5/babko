package com.business.order.application.service;

import com.business.common.application.exception.BusinessLogicException;
import com.business.order.application.dto.request.OrderCreateRequestDto;
import com.business.order.application.dto.request.OrderItemRequestDto;
import com.business.order.application.dto.response.OrderCreateResponseDto;
import com.business.order.application.dto.mapper.OrderMapper;
import com.business.order.application.dto.response.OrderGetResponseDto;
import com.business.order.application.exception.OrderExceptionCode;
import com.business.order.domain.entity.Order;
import com.business.order.domain.entity.OrderItem;
import com.business.order.domain.repository.OrderRepository;
import com.business.order.infrastructure.client.ProductFeignClient;
import com.business.order.infrastructure.dto.response.ProductDetailResponseDto;
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
//    private final DeliveryFeignClient deliveryFeignClient;
    private final ProductFeignClient productFeignClient;

    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto request, Long userId){
        List<OrderItemRequestDto> items = request.getItems();

        for (OrderItemRequestDto item : items) {
            ProductDetailResponseDto productDetail = productFeignClient.getProductDetail(item.getProductId());

            if(item.getOrderItemAmount() > productDetail.getProductQuantity()){
                throw new BusinessLogicException(OrderExceptionCode.PRODUCT_QUANTITY_EXCEEDED);
            }

            item.setSupplierId(productDetail.getSupplierId());
            item.setOrderItemPrice(productDetail.getProductPrice()); //여기서 매핑
        }

        UUID originHubId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID destinationHubId = UUID.fromString("00000000-0000-0000-0000-000000000002");

        Order order = request.createOrder(userId, originHubId, destinationHubId);

        List<OrderItem> orderItems = items.stream()
                .map(i -> i.createOrderItem(order))
                .collect(Collectors.toList());

        order.addOrderItems(orderItems);
        Order savedOrder  = orderRepository.save(order);

//        OrderDeliveryRequestDto deliveryRequest = OrderDeliveryRequestDto.fromOrder(savedOrder);
//        if(!deliveryFeignClient.createDelivery(deliveryRequest)){
//            throw new BusinessLogicException(OrderExceptionCode.DELIVERY_REQUEST_FAILED);
//        }

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
}
