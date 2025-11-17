package com.commerce.domain.order.domain;

public enum OrderStatus {
  PENDING,
  CANCELLED,
  PAYMENT_SUCCESS,
  PAYMENT_FAILED,
  PAYMENT_CANCELLED,
  SHIPPING_WAITING,
  SHIPPED,
  SHIPPING_FINISH
}
