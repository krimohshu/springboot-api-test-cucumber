package com.microservices.order.controller.v1;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.microservices.order.dto.v1.OrderRequest;
import com.microservices.order.dto.v1.OrderResponse;
import com.microservices.order.service.v1.OrderService;

@RestController("orderControllerV1")
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management V1 API", description = "Basic CRUD operations for orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieves all orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieves an order by its ID")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by order number", description = "Retrieves an order by its order number")
    public ResponseEntity<OrderResponse> getOrderByOrderNumber(@PathVariable String orderNumber) {
        return ResponseEntity.ok(orderService.getOrderByOrderNumber(orderNumber));
    }

    @PostMapping
    @Operation(summary = "Create new order", description = "Creates a new order")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order", description = "Updates an existing order")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order", description = "Deletes an order")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get orders by user ID", description = "Retrieves all orders for a specific user")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get orders by product ID", description = "Retrieves all orders for a specific product")
    public ResponseEntity<List<OrderResponse>> getOrdersByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(orderService.getOrdersByProductId(productId));
    }
}
