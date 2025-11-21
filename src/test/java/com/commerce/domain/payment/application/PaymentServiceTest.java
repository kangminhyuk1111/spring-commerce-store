package com.commerce.domain.payment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.commerce.domain.order.application.OrderService;
import com.commerce.domain.order.domain.Order;
import com.commerce.domain.order.domain.OrderStatus;
import com.commerce.domain.payment.domain.Payment;
import com.commerce.domain.payment.domain.PaymentStatus;
import com.commerce.domain.payment.repository.FakePaymentRepository;
import com.commerce.domain.payment.repository.PaymentRepository;
import com.commerce.domain.point.application.PointService;
import com.commerce.domain.point.domain.Point;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

  @Mock
  private OrderService orderService;

  @Mock
  private PointService pointService;

  private PaymentRepository paymentRepository;
  private PaymentService paymentService;

  @BeforeEach
  void setUp() {
    paymentRepository = new FakePaymentRepository();
    paymentService = new PaymentService(paymentRepository, orderService, pointService);
  }

  @Nested
  class 결제_생성 {

    @Test
    void 결제를_생성한다() {
      // given
      Long userId = 1L;
      BigDecimal totalPrice = BigDecimal.valueOf(50000);
      BigDecimal usingPoint = BigDecimal.valueOf(5000);

      Order order = createOrder(userId, totalPrice);

      when(pointService.getPoint(userId)).thenReturn(new Point(userId, BigDecimal.valueOf(10000)));
      when(orderService.getOrder(userId, order.getOrderKey())).thenReturn(order);

      // when
      paymentService.createPayment(userId, order.getOrderKey(), usingPoint);

      // then
      Payment savedPayment = paymentRepository.findByOrderId(order.getId()).orElseThrow();
      assertThat(savedPayment.getOrderId()).isEqualTo(order.getId());
      assertThat(savedPayment.getUserId()).isEqualTo(userId);
      assertThat(savedPayment.getFinalAmount()).isEqualTo(totalPrice.subtract(usingPoint));
      assertThat(savedPayment.getUsedPoint()).isEqualTo(usingPoint);
      assertThat(savedPayment.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
    }
  }

  @Nested
  class 결제_성공 {

    @Test
    void 결제가_성공하면_주문상태가_변경되고_포인트가_차감된다() {
      // given
      String orderKey = "ORDER-20250121-001";
      String externalPaymentKey = "PG-KEY-12345";
      Long userId = 1L;
      BigDecimal totalAmount = BigDecimal.valueOf(50000);
      BigDecimal usedPoint = BigDecimal.valueOf(5000);

      Order order = createOrder(orderKey, userId, totalAmount);
      Long orderId = order.getId();

      Payment payment = new Payment(orderId, userId, totalAmount, usedPoint);
      paymentRepository.save(payment);

      when(orderService.findOrderByKey(orderKey)).thenReturn(order);

      // when
      paymentService.success(orderKey, externalPaymentKey, payment.getFinalAmount());

      // then
      Payment successPayment = paymentRepository.findByOrderId(orderId).orElseThrow();
      assertThat(successPayment.getPaymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
      assertThat(successPayment.getExternalPaymentKey()).isEqualTo(externalPaymentKey);
      assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PAYMENT_SUCCESS);

      verify(pointService).usePoint(userId, usedPoint);
    }
  }

  @Nested
  class 결제_실패 {

    @Test
    void 결제가_실패하면_주문상태와_결제상태가_실패로_변경된다() {
      // given
      String orderKey = "ORDER-20250121-002";
      String errorCode = "CARD_DECLINED";
      String errorMessage = "카드 한도 초과";
      Long userId = 1L;
      BigDecimal totalPrice = BigDecimal.valueOf(100000);
      BigDecimal usedPoint = BigDecimal.ZERO;

      Order order = createOrder(orderKey, userId, totalPrice);
      Long orderId = order.getId();

      Payment payment = new Payment(orderId, userId, totalPrice, usedPoint);
      paymentRepository.save(payment);

      // Mock 설정
      when(orderService.findOrderByKey(orderKey)).thenReturn(order);

      // when
      paymentService.fail(orderKey, errorCode, errorMessage);

      // then
      Payment failedPayment = paymentRepository.findByOrderId(orderId).orElseThrow();
      assertThat(failedPayment.getPaymentStatus()).isEqualTo(PaymentStatus.FAIL);
      assertThat(failedPayment.getFailureCode()).isEqualTo(errorCode);
      assertThat(failedPayment.getFailureReason()).isEqualTo(errorMessage);
      assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PAYMENT_FAILED);
    }
  }

  // 테스트 헬퍼 메서드 - 팩토리 패턴
  private Order createOrder(Long userId, BigDecimal totalPrice) {
    String orderKey = "TEST-ORDER-" + System.currentTimeMillis();
    return new Order(orderKey, userId, totalPrice, OrderStatus.PENDING, new ArrayList<>());
  }

  private Order createOrder(String orderKey, Long userId, BigDecimal totalPrice) {
    return new Order(orderKey, userId, totalPrice, OrderStatus.PENDING, new ArrayList<>());
  }
}

