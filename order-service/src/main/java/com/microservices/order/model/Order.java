package com.microservices.order.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders", indexes = {
    @Index(name = "idx_order_number", columnList = "orderNumber"),
    @Index(name = "idx_user_id", columnList = "userId"),
    @Index(name = "idx_product_id", columnList = "productId"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_order_date", columnList = "orderDate")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Order number is required")
    @Size(max = 50, message = "Order number cannot exceed 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String orderNumber;

    @NotNull(message = "User ID is required")
    @Column(nullable = false)
    private Long userId;

    @NotNull(message = "Product ID is required")
    @Column(nullable = false)
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @NotBlank(message = "Status is required")
    @Size(max = 20, message = "Status cannot exceed 20 characters")
    @Column(nullable = false, length = 20)
    private String status; // PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Size(max = 200, message = "Shipping address cannot exceed 200 characters")
    @Column(length = 200)
    private String shippingAddress;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        orderDate = LocalDateTime.now();
        if (active == null) {
            active = true;
        }
        if (status == null) {
            status = "PENDING";
        }
        // Calculate total price if not set
        if (totalPrice == null && unitPrice != null && quantity != null) {
            totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        // Recalculate total price on update
        if (unitPrice != null && quantity != null) {
            totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
