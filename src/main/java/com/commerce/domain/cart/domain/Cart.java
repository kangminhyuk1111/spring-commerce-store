package com.commerce.domain.cart.domain;

import com.commerce.controller.dto.response.CartItemResponse;
import com.commerce.domain.order.domain.NewOrder;
import com.commerce.domain.order.domain.NewOrderItem;
import com.commerce.support.error.CoreException;
import com.commerce.support.error.ErrorType;
import java.util.List;
import java.util.stream.Collectors;

public record Cart(
    Long userId,
    List<CartItemResponse> items
) {
  public NewOrder toNewOrder(List<Long> targetItemIds) {
    if (items.isEmpty()) {
      throw new CoreException(ErrorType.CART_IS_EMPTY);
    }

    List<NewOrderItem> orderItems = items.stream()
        .filter(item -> targetItemIds.contains(item.cartItemId()))
        .map(item -> new NewOrderItem(
            item.product().getId(),
            item.quantity()
        ))
        .collect(Collectors.toList());

    return new NewOrder(
        userId,
        orderItems
    );
  }
}
