package com.commerce.domain.order.repository;

import com.commerce.domain.order.domain.Order;
import com.commerce.domain.order.domain.OrderItem;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
  Optional<Order> findByOrderKey(String orderKey);
  Order save(Order order);
  List<OrderItem> findOrderItemsByOrderId(Long orderId);
  List<Order> findByUserId(Long userId);
}
