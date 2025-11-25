package com.microservices.order.dto.v2;

import java.math.BigDecimal;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatsResponse {
    private long totalOrders;
    private long activeOrders;
    private long inactiveOrders;
    private Map<String, Long> ordersByStatus;
    private Map<Long, Long> ordersByUser;
    private Map<Long, Long> ordersByProduct;
    private BigDecimal totalRevenue;
    private BigDecimal pendingRevenue;
    private BigDecimal completedRevenue;
}
