package com.commerce.controller.dto.response;

import com.commerce.domain.order.domain.Order;
import com.commerce.domain.order.domain.OrderItem;
import com.commerce.domain.point.domain.Point;
import java.math.BigDecimal;
import java.util.List;

public record OrderCheckoutResponse(
    String orderKey,
    BigDecimal totalPrice,
    List<OrderItem> orderItems,
    BigDecimal usablePoint
) {
  public static OrderCheckoutResponse of(Order order, Point point) {
    return new OrderCheckoutResponse(
        order.getOrderKey(),
        order.getTotalPrice(),
        order.getItems(),
        point.getBalance()
    );
  }
}
