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
  private PaymentMethod paymentMethod;

  @Column(nullable = false)
  private BigDecimal usedPoint;

  private String externalPaymentKey;

  private String transactionId;

  private String failureCode;

  private String failureReason;

  public void success(String externalPaymentKey, PaymentMethod paymentMethod, String transactionId) {
    this.paymentStatus = PaymentStatus.SUCCESS;
    this.externalPaymentKey = externalPaymentKey;
    this.paymentMethod = paymentMethod;
    this.transactionId = transactionId;
  }

  public void fail(String failureCode, String failureReason) {
    this.paymentStatus = PaymentStatus.FAIL;
    this.failureCode = failureCode;
    this.failureReason = failureReason;
  }

  public Boolean isPaid() {
    return this.paymentStatus == PaymentStatus.SUCCESS;
  }

  public Boolean isFailed() {
    return this.paymentStatus == PaymentStatus.FAIL;
  }

  public Payment() {

  }

  public Payment(Long orderId, Long userId, BigDecimal totalAmount, BigDecimal usedPoint) {
    this.orderId = orderId;
    this.userId = userId;
    this.totalAmount = totalAmount;
    this.usedPoint = usedPoint;
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

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public BigDecimal getUsedPoint() {
    return usedPoint;
  }

  public String getExternalPaymentKey() {
    return externalPaymentKey;
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
