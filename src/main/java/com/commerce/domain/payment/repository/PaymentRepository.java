package com.commerce.domain.payment.repository;

import com.commerce.domain.payment.domain.Payment;
import java.util.Optional;

public interface PaymentRepository {

  Optional<Payment> findByOrderId(Long id);

  Payment save(Payment payment);
}
