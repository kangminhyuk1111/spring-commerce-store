package com.commerce.domain.payment.repository.impl;

import com.commerce.domain.payment.domain.Payment;
import com.commerce.domain.payment.repository.PaymentRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

  private final JpaPaymentRepository jpaPaymentRepository;

  public PaymentRepositoryImpl(JpaPaymentRepository jpaPaymentRepository) {
    this.jpaPaymentRepository = jpaPaymentRepository;
  }

  @Override
  public Optional<Payment> findByOrderId(Long orderId) {
    return jpaPaymentRepository.findByOrderId(orderId);
  }

  @Override
  public Payment save(Payment payment) {
    return jpaPaymentRepository.save(payment);
  }
}
