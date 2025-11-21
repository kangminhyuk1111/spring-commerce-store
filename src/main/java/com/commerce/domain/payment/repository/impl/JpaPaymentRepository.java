package com.commerce.domain.payment.repository.impl;

import com.commerce.domain.payment.domain.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPaymentRepository extends JpaRepository<Payment, Long> {

  Optional<Payment> findByOrderId(Long orderId);
}
