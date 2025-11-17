package com.commerce.domain.order.domain;

import java.math.BigDecimal;
import java.util.List;

public record OrderRecord(
    Long id,
    String orderKey,
    Long userId,
    BigDecimal totalPrice,
    OrderStatus orderStatus,
    List<OrderItem> items
) {
}
