package com.commerce.controller.dto.response;

import com.commerce.domain.order.domain.Order;
import com.commerce.domain.point.domain.Point;

public record OrderCheckoutResponse() {
  public static OrderCheckoutResponse of(Order order, Point point) {
    return new OrderCheckoutResponse();
  }
}
