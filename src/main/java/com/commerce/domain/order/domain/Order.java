package com.commerce.domain.order.domain;

import com.commerce.support.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

  @Column(nullable = false)
  private String orderKey;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private BigDecimal totalPrice;

  @Column(nullable = false)
  private OrderStatus orderStatus;

  private PayType payType;

  private String shippingAddress;

  // CascadeType.ALL -> order삭제시 orderItem도 삭제
  // orphanRemoval -> items에서 remove되면 자동 삭제(OrderItem)
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();

  public Order() {
  }

  public Order(String orderKey, Long userId, BigDecimal totalPrice, OrderStatus orderStatus, List<OrderItem> items) {
    this.orderKey = orderKey;
    this.userId = userId;
    this.totalPrice = totalPrice;
    this.orderStatus = orderStatus;
    this.items = items;
  }

  public void updatePayType(PayType payType) {
    this.payType = payType;
  }

  public void updateShippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public void updateStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public void cancel() {
    this.orderStatus = OrderStatus.CANCELLED;
  }

  public Boolean isCancellable() {
    return this.orderStatus.equals(OrderStatus.PENDING)
        || this.orderStatus.equals(OrderStatus.PAYMENT_CANCELLED)
        || this.orderStatus.equals(OrderStatus.PAYMENT_FAILED)
        || this.orderStatus.equals(OrderStatus.PAYMENT_SUCCESS);
  }

  public String getOrderKey() {
    return orderKey;
  }

  public Long getUserId() {
    return userId;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public PayType getPayType() {
    return payType;
  }

  public String getShippingAddress() {
    return shippingAddress;
  }

  public List<OrderItem> getItems() {
    return items;
  }
}
