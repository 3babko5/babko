package com.business.order.application.service;

import com.business.order.application.dto.request.OrderCreateRequestDto;
import com.business.order.application.dto.response.OrderCreateResponseDto;
import com.business.order.application.mapper.OrderMapper;
import com.business.order.domain.entity.Order;
import com.business.order.domain.entity.OrderItem;
import com.business.order.domain.repository.OrderItemRepository;
import com.business.order.domain.repository.OrderRepository;
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
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto request, Long userId){


        UUID originHubId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID destinationHubId = UUID.fromString("00000000-0000-0000-0000-000000000002");

        Order order = Order.create(
                userId,
                request.getReceiverId(),
                request.getDeliveryAddress(),
                originHubId,
                destinationHubId,
                25000
        );
        Order savedOrder  = orderRepository.save(order);

        UUID supplierId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        List<OrderItem> orderItems = request.getItems().stream()
                .map(item -> OrderItem.create(
                        savedOrder,
                        item.getProductId(),
                        supplierId,
                        item.getOrderItemAmount(),
                        10000L
                ))
                .collect(Collectors.toList());

        savedOrder.addOrderItems(orderItems);

        return OrderMapper.toOrderCreateResponseDto(savedOrder, orderItems);
    }

}
