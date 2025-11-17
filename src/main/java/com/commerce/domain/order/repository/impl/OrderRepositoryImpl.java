package com.commerce.domain.order.repository.impl;

import com.commerce.domain.order.domain.Order;
import com.commerce.domain.order.domain.OrderItem;
import com.commerce.domain.order.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

  private final JpaOrderRepository jpaOrderRepository;
  private final JpaOrderItemRepository jpaOrderItemRepository;

  public OrderRepositoryImpl(JpaOrderRepository jpaOrderRepository, JpaOrderItemRepository jpaOrderItemRepository) {
    this.jpaOrderRepository = jpaOrderRepository;
    this.jpaOrderItemRepository = jpaOrderItemRepository;
  }

  @Override
  public Optional<Order> findByOrderKey(String orderKey) {
    return jpaOrderRepository.findByOrderKey(orderKey);
  }

  @Override
  public Order save(Order order) {
    Order savedOrder = jpaOrderRepository.save(order);

    List<OrderItem> orderItems = order.getItems().stream()
        .map(item -> new OrderItem(
            item.getProductId(),
            savedOrder.getId(),
            item.getProductName(),
            item.getProductPrice(),
            item.getQuantity(),
            item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        ))
        .toList();

    jpaOrderItemRepository.saveAll(orderItems);
    return savedOrder;
  }

  @Override
  public List<OrderItem> findOrderItemsByOrderId(Long orderId) {
    return jpaOrderItemRepository.findByOrderId(orderId);
  }

  @Override
  public List<Order> findByUserId(Long userId) {
    return jpaOrderRepository.findByUserId(userId);
  }
}
