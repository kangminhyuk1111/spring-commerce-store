package com.commerce.domain.order.repository.impl;

import com.commerce.domain.order.domain.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaRepository<Order, Long> {
  Optional<Order> findByOrderKey(String orderKey);

  List<Order> findByUserId(Long userId);
}
