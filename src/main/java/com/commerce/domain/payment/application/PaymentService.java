package com.commerce.domain.payment.application;

import com.commerce.domain.order.application.OrderService;
import com.commerce.domain.order.domain.Order;
import com.commerce.domain.order.domain.OrderStatus;
import com.commerce.domain.payment.domain.Payment;
import com.commerce.controller.dto.PaymentInfo;
import com.commerce.domain.payment.domain.PaymentMethod;
import com.commerce.domain.payment.domain.PaymentStatus;
import com.commerce.domain.payment.repository.PaymentRepository;
import com.commerce.domain.point.application.PointService;
import com.commerce.support.error.CoreException;
import com.commerce.support.error.ErrorType;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final OrderService orderService;
  private final PointService pointService;

  public PaymentService(PaymentRepository paymentRepository, OrderService orderService, PointService pointService) {
    this.paymentRepository = paymentRepository;
    this.orderService = orderService;
    this.pointService = pointService;
  }

  /*
   * 결제 버튼 클릭시 결제 정보 생성
   * */
  @Transactional
  public void createPayment(Order order, PaymentInfo paymentInfo) {
    paymentRepository.findByOrderId(order.getId())
        .filter(Payment::isPaid)
        .ifPresent(p -> {
          throw new CoreException(ErrorType.PAYMENT_ALREADY_PAID);
        });

    BigDecimal totalPayAmount = order.getTotalPrice().subtract(paymentInfo.usingPoint());

    Payment payment = new Payment(order.getId(), order.getUserId(), totalPayAmount, paymentInfo.usingPoint());

    paymentRepository.save(payment);
  }

  /*
  * 결제 성공
  * */
  @Transactional
  public void success(String orderKey, String externalPaymentKey, BigDecimal amount) {
    Order order = orderService.findOrderByKey(orderKey);

    Payment payment = paymentRepository.findByOrderId(order.getId()).orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND_DATA));

    validatePayment(order, payment);

    /*
    * amount 만큼 결제
    * PG사 승인 API -> 성공 시 다음 진행, 실패시 예외 발생
    * */

    payment.success(externalPaymentKey, PaymentMethod.CREDIT_CARD, "카드사, 결제사에서 발급한 transactionId");
    order.paid();

    pointService.usePoint(order.getUserId(), payment.getUsedPoint());
  }

  /*
  * 결제 실패
  * */
  @Transactional
  public void fail(String orderKey, String code, String message) {
    Order order = orderService.findOrderByKey(orderKey);
    Payment payment = paymentRepository.findByOrderId(order.getId()).orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND_DATA));

    if (payment.isFailed()) {
      return;
    }

    order.updateStatus(OrderStatus.PAYMENT_FAILED);
    payment.fail(code, message);
  }

  private void validatePayment(Order order, Payment payment) {
    if (!payment.getUserId().equals(order.getUserId())) {
      throw new CoreException(ErrorType.NOT_FOUND_DATA);
    }

    if (!payment.getPaymentStatus().equals(PaymentStatus.PENDING)) {
      throw new CoreException(ErrorType.PAYMENT_STATUS_NOT_PENDING);
    }

    if (!payment.getTotalAmount().equals(order.getTotalPrice())) {
      throw new CoreException(ErrorType.PAYMENT_PRICE_NOT_EQUALS);
    }
  }
}
