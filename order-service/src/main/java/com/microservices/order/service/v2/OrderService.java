package com.microservices.order.service.v2;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.microservices.order.dto.v2.BulkOrderRequest;
import com.microservices.order.dto.v2.OrderFilterRequest;
import com.microservices.order.dto.v2.OrderRequest;
import com.microservices.order.dto.v2.OrderResponse;
import com.microservices.order.dto.v2.OrderStatsResponse;
import com.microservices.order.dto.v2.PagedResponse;
import com.microservices.order.exception.OrderNotFoundException;
import com.microservices.order.model.Order;
import com.microservices.order.repository.OrderRepository;

@Service("orderServiceV2")
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public PagedResponse<OrderResponse> searchOrders(OrderFilterRequest filterRequest) {
        Pageable pageable = PageRequest.of(
            filterRequest.getPage(),
            filterRequest.getSize(),
            Sort.by("orderDate").descending()
        );

        Page<Order> orderPage = orderRepository.searchOrders(
            filterRequest.getUserId(),
            filterRequest.getProductId(),
            filterRequest.getStatus(),
            filterRequest.getActive(),
            filterRequest.getOrderNumber(),
            filterRequest.getStartDate(),
            filterRequest.getEndDate(),
            pageable
        );

        List<OrderResponse> orders = orderPage.getContent().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());

        return new PagedResponse<>(
            orders,
            orderPage.getNumber(),
            orderPage.getSize(),
            orderPage.getTotalElements(),
            orderPage.getTotalPages(),
            orderPage.isLast()
        );
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
        order.setStatus(request.getStatus() != null ? request.getStatus() : "PENDING");
        order.setShippingAddress(request.getShippingAddress());
        order.setNotes(request.getNotes());
        order.setActive(request.getActive() != null ? request.getActive() : true);

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
        order.setStatus(request.getStatus());
        order.setShippingAddress(request.getShippingAddress());
        order.setNotes(request.getNotes());
        if (request.getActive() != null) {
            order.setActive(request.getActive());
        }

        Order updatedOrder = orderRepository.save(order);
        return convertToResponse(updatedOrder);
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
        order.setActive(false);
        orderRepository.save(order);
    }

    @Transactional
    public List<OrderResponse> bulkCreateOrders(BulkOrderRequest bulkRequest) {
        List<Order> orders = bulkRequest.getOrders().stream()
            .map(request -> {
                Order order = new Order();
                order.setOrderNumber(request.getOrderNumber());
                order.setUserId(request.getUserId());
                order.setProductId(request.getProductId());
                order.setQuantity(request.getQuantity());
                order.setUnitPrice(request.getUnitPrice());
                order.setStatus(request.getStatus() != null ? request.getStatus() : "PENDING");
                order.setShippingAddress(request.getShippingAddress());
                order.setNotes(request.getNotes());
                order.setActive(request.getActive() != null ? request.getActive() : true);
                return order;
            })
            .collect(Collectors.toList());

        List<Order> savedOrders = orderRepository.saveAll(orders);
        return savedOrders.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getAllStatuses() {
        return orderRepository.findDistinctStatuses();
    }

    @Transactional(readOnly = true)
    public OrderStatsResponse getOrderStatistics() {
        long totalOrders = orderRepository.count();
        long activeOrders = orderRepository.countByActive(true);
        long inactiveOrders = orderRepository.countByActive(false);

        List<Object[]> statusData = orderRepository.countByStatus();
        Map<String, Long> ordersByStatus = statusData.stream()
            .filter(row -> row[0] != null)
            .collect(Collectors.toMap(
                row -> (String) row[0],
                row -> ((Number) row[1]).longValue()
            ));

        List<Object[]> userData = orderRepository.countByUser();
        Map<Long, Long> ordersByUser = userData.stream()
            .filter(row -> row[0] != null)
            .limit(10) // Top 10 users
            .collect(Collectors.toMap(
                row -> ((Number) row[0]).longValue(),
                row -> ((Number) row[1]).longValue()
            ));

        List<Object[]> productData = orderRepository.countByProduct();
        Map<Long, Long> ordersByProduct = productData.stream()
            .filter(row -> row[0] != null)
            .limit(10) // Top 10 products
            .collect(Collectors.toMap(
                row -> ((Number) row[0]).longValue(),
                row -> ((Number) row[1]).longValue()
            ));

        BigDecimal totalRevenue = orderRepository.sumTotalPriceByStatus("DELIVERED");
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;

        BigDecimal pendingRevenue = orderRepository.sumTotalPriceByStatus("PENDING");
        if (pendingRevenue == null) pendingRevenue = BigDecimal.ZERO;

        BigDecimal completedRevenue = totalRevenue;

        return new OrderStatsResponse(
            totalOrders,
            activeOrders,
            inactiveOrders,
            ordersByStatus,
            ordersByUser,
            ordersByProduct,
            totalRevenue,
            pendingRevenue,
            completedRevenue
        );
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return convertToResponse(updatedOrder);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateUserTotalSpending(Long userId) {
        BigDecimal total = orderRepository.sumTotalPriceByUserId(userId);
        return total != null ? total : BigDecimal.ZERO;
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
            order.getActive(),
            order.getCreatedAt(),
            order.getUpdatedAt(),
            order.getVersion()
        );
    }
}
