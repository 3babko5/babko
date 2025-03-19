package com.business.order.application.service;

import com.business.order.application.dto.request.OrderCreateRequestDto;
import com.business.order.application.dto.response.OrderCreateResponseDto;
import com.business.order.application.mapper.OrderMapper;
import com.business.order.domain.entity.Order;
import com.business.order.domain.entity.OrderItem;
import com.business.order.domain.repository.OrderItemRepository;
import com.business.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    //주문 생성 (외부 서비스 없이 내부 로직만)
    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto request, Long userId){

        //상품 서비스에서 productId 기반으로 supplierId 조회 (현재는 더미 데이터)
        UUID supplierId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        //허브 서비스에서 supplierId, receiverId 기반으로 hubId 조회 (현재는 더미 데이터)
        UUID originHubId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID destinationHubId = UUID.fromString("00000000-0000-0000-0000-000000000002");

        //주문 생성
        Order order = Order.create(
                userId,
                request.getReceiverId(),
                request.getDeliveryAddress(),
                originHubId,
                destinationHubId,
                25000
        );
        Order savedOrder  = orderRepository.save(order);

        //주문 아이템 생성(상품 가격 더미 데이터 활용)
        List<OrderItem> orderItems = request.getItems().stream()
                .map(item -> OrderItem.create(
                        savedOrder,
                        item.getProductId(),
                        item.getOrderItemAmount(),
                        10000L
                ))
                .collect(Collectors.toList());

        savedOrder.addOrderItems(orderItems);
        List<OrderItem> saveOrderItems = orderItemRepository.saveAll(orderItems);

        return OrderMapper.toOrderCreateResponseDto(savedOrder, saveOrderItems);
    }

}
