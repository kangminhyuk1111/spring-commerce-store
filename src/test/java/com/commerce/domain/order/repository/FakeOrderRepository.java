package com.commerce.domain.order.repository;

import com.commerce.domain.order.domain.Order;
import com.commerce.domain.order.domain.OrderItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeOrderRepository implements OrderRepository {

  private final Map<Long, Order> orderStore = new HashMap<>();
  private final Map<String, Order> orderKeyIndex = new HashMap<>();
  private final Map<Long, List<OrderItem>> orderItemStore = new HashMap<>();
  private long orderSequence = 0L;
  private long orderItemSequence = 0L;

  @Override
  public Optional<Order> findByOrderKey(String orderKey) {
    return Optional.ofNullable(orderKeyIndex.get(orderKey));
  }

  @Override
  public Order save(Order order) {
    if (order.getId() == null) {
      setId(order, ++orderSequence);
    }

    orderStore.put(order.getId(), order);
    orderKeyIndex.put(order.getOrderKey(), order);

    if (order.getId() != null && order.getItems() != null && !order.getItems().isEmpty()) {
      orderItemStore.remove(order.getId());

      for (OrderItem item : order.getItems()) {
        setOrderIdToItem(item, order.getId());
        if (item.getId() == null) {
          setIdToOrderItem(item, ++orderItemSequence);
        }
        saveOrderItem(item);
      }
    }

    return order;
  }

  @Override
  public List<OrderItem> findOrderItemsByOrderId(Long orderId) {
    return new ArrayList<>(orderItemStore.getOrDefault(orderId, new ArrayList<>()));
  }

  @Override
  public List<Order> findByUserId(Long userId) {
    return orderStore.values().stream()
        .filter(order -> order.getUserId().equals(userId))
        .toList();
  }

  public void saveOrderItem(OrderItem orderItem) {
    Long orderId = orderItem.getOrderId();
    if (orderId == null) {
      throw new IllegalArgumentException("OrderItem의 orderId가 null입니다: " + orderItem);
    }
    orderItemStore.computeIfAbsent(orderId, k -> new ArrayList<>()).add(orderItem);
  }

  private void setId(Order entity, long id) {
    try {
      java.lang.reflect.Field field = entity.getClass().getSuperclass()
          .getDeclaredField("id");
      field.setAccessible(true);
      field.set(entity, id);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setOrderIdToItem(OrderItem orderItem, Long orderId) {
    try {
      java.lang.reflect.Field field = OrderItem.class.getDeclaredField("orderId");
      field.setAccessible(true);
      field.set(orderItem, orderId);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException("orderId 필드를 찾을 수 없습니다", e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("orderId 필드에 접근할 수 없습니다", e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setIdToOrderItem(OrderItem orderItem, long id) {
    try {
      java.lang.reflect.Field field = orderItem.getClass().getSuperclass()
          .getDeclaredField("id");
      field.setAccessible(true);
      field.set(orderItem, id);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}