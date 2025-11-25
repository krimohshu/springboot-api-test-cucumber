package com.microservices.order.dto.v2;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilterRequest {
    private String orderNumber;
    private Long userId;
    private Long productId;
    private String status;
    private Boolean active;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Min(value = 0, message = "Page number must be 0 or greater")
    private Integer page = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    private Integer size = 10;
}
