package com.commerce.domain.order.application;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class OrderKeyGenerator {

  public String generate(Long userId) {
    return UUID.randomUUID().toString() + userId;
  }
}
