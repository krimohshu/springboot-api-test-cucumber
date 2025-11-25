package com.microservices.order.controller.v2;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.microservices.order.dto.v2.BulkOrderRequest;
import com.microservices.order.dto.v2.OrderFilterRequest;
import com.microservices.order.dto.v2.OrderRequest;
import com.microservices.order.dto.v2.OrderResponse;
import com.microservices.order.dto.v2.OrderStatsResponse;
import com.microservices.order.dto.v2.PagedResponse;
import com.microservices.order.service.v2.OrderService;

@RestController("orderControllerV2")
@RequestMapping("/api/v2/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management V2", description = "Enhanced order operations with pagination, filtering, and bulk operations")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/search")
    @Operation(summary = "Search orders with pagination", description = "Search and filter orders with pagination support")
    public ResponseEntity<PagedResponse<OrderResponse>> searchOrders(@Valid @RequestBody OrderFilterRequest filterRequest) {
        return ResponseEntity.ok(orderService.searchOrders(filterRequest));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieves an order with all v2 fields")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by order number", description = "Retrieves an order by its order number")
    public ResponseEntity<OrderResponse> getOrderByOrderNumber(@PathVariable String orderNumber) {
        return ResponseEntity.ok(orderService.getOrderByOrderNumber(orderNumber));
    }

    @PostMapping
    @Operation(summary = "Create new order", description = "Creates a new order with status and active fields")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order", description = "Updates an existing order")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete order", description = "Soft deletes an order by setting active to false")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk create orders", description = "Creates multiple orders in a single request")
    public ResponseEntity<List<OrderResponse>> bulkCreateOrders(@Valid @RequestBody BulkOrderRequest bulkRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.bulkCreateOrders(bulkRequest));
    }

    @GetMapping("/statuses")
    @Operation(summary = "Get all statuses", description = "Retrieves all distinct order statuses")
    public ResponseEntity<List<String>> getAllStatuses() {
        return ResponseEntity.ok(orderService.getAllStatuses());
    }

    @GetMapping("/stats")
    @Operation(summary = "Get order statistics", description = "Retrieves order statistics including counts by status, user, product, and revenue")
    public ResponseEntity<OrderStatsResponse> getOrderStatistics() {
        return ResponseEntity.ok(orderService.getOrderStatistics());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Updates the status of an order")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @GetMapping("/user/{userId}/total-spending")
    @Operation(summary = "Calculate user total spending", description = "Calculates the total spending amount for a user")
    public ResponseEntity<Map<String, BigDecimal>> calculateUserTotalSpending(@PathVariable Long userId) {
        BigDecimal total = orderService.calculateUserTotalSpending(userId);
        return ResponseEntity.ok(Map.of("totalSpending", total));
    }
}
