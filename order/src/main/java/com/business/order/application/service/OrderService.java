package com.business.order.application.service;

import com.business.common.application.exception.BusinessLogicException;
import com.business.order.application.dto.request.OrderCreateRequestDto;
import com.business.order.application.dto.response.OrderCreateResponseDto;
import com.business.order.application.dto.mapper.OrderMapper;
import com.business.order.application.exception.OrderExceptionCode;
import com.business.order.domain.entity.Order;
import com.business.order.domain.entity.OrderItem;
import com.business.order.domain.repository.OrderRepository;
import com.business.order.infrastructure.client.DeliveryFeignClient;
import com.business.order.infrastructure.dto.request.OrderDeliveryRequestDto;
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

    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto request, Long userId){

        UUID originHubId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID destinationHubId = UUID.fromString("00000000-0000-0000-0000-000000000002");

        Order order = request.createOrder(userId, originHubId, destinationHubId);
        Order savedOrder  = orderRepository.save(order);

        List<OrderItem> orderItems = request.getItems()
                .stream()
                .map(i -> {
                    UUID supplierId = UUID.fromString("00000000-0000-0000-0000-000000000000");
                    Long productPrice = 10000L;

                    OrderItem orderItem = i.createOrderItem(order, supplierId);
                    orderItem.setOrderItemPrice(productPrice);
                    return orderItem;
                })
                .collect(Collectors.toList());

        savedOrder.addOrderItems(orderItems);

        OrderDeliveryRequestDto deliveryRequest = OrderDeliveryRequestDto.fromOrder(savedOrder);
        if(!deliveryFeignClient.createDelivery(deliveryRequest)){
            throw new BusinessLogicException(OrderExceptionCode.DELIVERY_REQUEST_FAILED);
        }

        return OrderMapper.toOrderCreateResponseDto(savedOrder, orderItems);
    }

}
