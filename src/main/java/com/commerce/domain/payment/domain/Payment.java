package com.commerce.domain.payment.domain;

import com.commerce.support.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

  @Column(nullable = false)
  private Long orderId;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private PaymentStatus paymentStatus = PaymentStatus.PENDING;

  @Column(nullable = false)
  private BigDecimal totalAmount;

  @Column(nullable = false)
  private String paymentMethod;

  private String transactionId;

  private String failureCode;

  private String failureReason;

  public Payment() {

  }

  public Long getOrderId() {
    return orderId;
  }

  public Long getUserId() {
    return userId;
  }

  public PaymentStatus getPaymentStatus() {
    return paymentStatus;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public String getFailureCode() {
    return failureCode;
  }

  public String getFailureReason() {
    return failureReason;
  }
}
