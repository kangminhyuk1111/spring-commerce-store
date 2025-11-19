package com.commerce.domain.payment.repository;

import com.commerce.domain.payment.domain.Payment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakePaymentRepository implements PaymentRepository {

  private final Map<Long, Payment> paymentStore = new HashMap<>();
  private final Map<Long, Payment> orderIdIndex = new HashMap<>();
  private long paymentSequence = 0L;

  @Override
  public Optional<Payment> findByOrderId(Long orderId) {
    return Optional.ofNullable(orderIdIndex.get(orderId));
  }

  @Override
  public Payment save(Payment payment) {
    if (payment.getId() == null) {
      setId(payment, ++paymentSequence);
    }

    paymentStore.put(payment.getId(), payment);
    orderIdIndex.put(payment.getOrderId(), payment);

    return payment;
  }

  // 테스트를 위한 추가 메서드
  public void clear() {
    paymentStore.clear();
    orderIdIndex.clear();
    paymentSequence = 0L;
  }

  public int size() {
    return paymentStore.size();
  }

  public Optional<Payment> findById(Long id) {
    return Optional.ofNullable(paymentStore.get(id));
  }

  private void setId(Payment payment, long id) {
    try {
      java.lang.reflect.Field field = payment.getClass().getSuperclass()
          .getDeclaredField("id");
      field.setAccessible(true);
      field.set(payment, id);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}