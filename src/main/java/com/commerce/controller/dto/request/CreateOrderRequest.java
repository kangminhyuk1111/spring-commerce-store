package com.commerce.controller.dto.request;

import com.commerce.domain.order.domain.NewOrder;
import com.commerce.domain.order.domain.NewOrderItem;
import java.util.List;

public record CreateOrderRequest(
    Long productId,
    Integer quantity
) {
  public NewOrder toNewOrder(Long userId) {
    List<NewOrderItem> orderItems = List.of(new NewOrderItem(productId, quantity));
    return new NewOrder(userId, orderItems);
  }
}
