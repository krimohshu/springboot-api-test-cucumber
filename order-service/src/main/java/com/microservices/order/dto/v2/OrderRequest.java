package com.microservices.order.dto.v2;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotBlank(message = "Order number is required")
    @Size(max = 50, message = "Order number cannot exceed 50 characters")
    private String orderNumber;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    private BigDecimal unitPrice;

    @Size(max = 20, message = "Status cannot exceed 20 characters")
    private String status; // PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED

    @Size(max = 200, message = "Shipping address cannot exceed 200 characters")
    private String shippingAddress;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    private Boolean active;
}
