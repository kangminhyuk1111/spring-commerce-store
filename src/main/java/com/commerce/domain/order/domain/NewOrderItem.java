package com.commerce.domain.order.domain;

public record NewOrderItem(
    Long productId,
    Integer quantity
) {
}
