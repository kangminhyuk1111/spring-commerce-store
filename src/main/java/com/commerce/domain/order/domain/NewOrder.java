package com.commerce.domain.order.domain;

import java.util.List;

public record NewOrder(
    Long userId,
    List<NewOrderItem> items
) {
}
