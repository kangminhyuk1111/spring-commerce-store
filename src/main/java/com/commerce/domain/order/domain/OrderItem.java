package com.commerce.domain.order.domain;

import com.commerce.support.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

  @Column(nullable = false)
  private Long productId;

  @Column(nullable = false)
  private Long orderId;

  @Column(nullable = false)
  private String productName;

  @Column(nullable = false)
  private BigDecimal productPrice;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private BigDecimal totalPrice;

  public OrderItem() {
  }

  public OrderItem(Long productId, Long orderId, String productName, BigDecimal productPrice, Integer quantity, BigDecimal totalPrice) {
    this.productId = productId;
    this.orderId = orderId;
    this.productName = productName;
    this.productPrice = productPrice;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
  }

  public Long getProductId() {
    return productId;
  }

  public Long getOrderId() {
    return orderId;
  }

  public String getProductName() {
    return productName;
  }

  public BigDecimal getProductPrice() {
    return productPrice;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }
}
