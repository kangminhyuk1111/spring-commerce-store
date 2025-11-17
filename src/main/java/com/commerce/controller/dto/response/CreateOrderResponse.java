package com.commerce.controller.dto.response;

public record CreateOrderResponse(
    String orderKey
) {

  public static CreateOrderResponse of(String orderKey) {
    return new CreateOrderResponse(orderKey);
  }
}
