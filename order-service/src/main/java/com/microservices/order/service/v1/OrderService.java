package com.microservices.order.service.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.microservices.order.dto.v1.OrderRequest;
import com.microservices.order.dto.v1.OrderResponse;
import com.microservices.order.exception.OrderNotFoundException;
import com.microservices.order.model.Order;
import com.microservices.order.repository.OrderRepository;

@Service("orderServiceV1")
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
        return convertToResponse(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with order number: " + orderNumber));
        return convertToResponse(order);
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Order order = new Order();
        order.setOrderNumber(request.getOrderNumber());
        order.setUserId(request.getUserId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(request.getUnitPrice());
        order.setShippingAddress(request.getShippingAddress());
        order.setNotes(request.getNotes());

        Order savedOrder = orderRepository.save(order);
        return convertToResponse(savedOrder);
    }

    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));

        order.setOrderNumber(request.getOrderNumber());
        order.setUserId(request.getUserId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(request.getUnitPrice());
        order.setShippingAddress(request.getShippingAddress());
        order.setNotes(request.getNotes());

        Order updatedOrder = orderRepository.save(order);
        return convertToResponse(updatedOrder);
    }

    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByProductId(Long productId) {
        return orderRepository.findByProductId(productId).stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    private OrderResponse convertToResponse(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getOrderNumber(),
            order.getUserId(),
            order.getProductId(),
            order.getQuantity(),
            order.getUnitPrice(),
            order.getTotalPrice(),
            order.getStatus(),
            order.getOrderDate(),
            order.getShippingAddress(),
            order.getNotes(),
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }
}
