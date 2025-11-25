package com.microservices.order.dto.v2;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkOrderRequest {
    
    @NotEmpty(message = "Orders list cannot be empty")
    @Valid
    private List<OrderRequest> orders;
}
