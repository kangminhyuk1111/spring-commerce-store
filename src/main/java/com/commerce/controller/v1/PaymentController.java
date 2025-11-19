package com.commerce.controller.v1;

import com.commerce.controller.dto.PaymentInfo;
import com.commerce.controller.dto.request.CreatePaymentRequest;
import com.commerce.domain.order.application.OrderService;
import com.commerce.domain.order.domain.Order;
import com.commerce.domain.payment.application.PaymentService;
import com.commerce.domain.point.application.PointService;
import com.commerce.domain.point.domain.Point;
import com.commerce.support.response.ApiResponse;
import java.math.BigDecimal;
import org.hibernate.mapping.Any;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

  private final PaymentService paymentService;
  private final PointService pointService;
  private final OrderService orderService;

  public PaymentController(PaymentService paymentService, PointService pointService,
      OrderService orderService) {
    this.paymentService = paymentService;
    this.pointService = pointService;
    this.orderService = orderService;
  }

  @PostMapping("/v1/payment")
  public ApiResponse<Any> createPayment(Long userId, @RequestBody CreatePaymentRequest request) {
    Order order = orderService.getOrder(userId, request.orderKey());
    Point point = pointService.getPoint(userId);
    PaymentInfo paymentInfo = new PaymentInfo(point.getBalance(), request.usingPoint());
    paymentService.createPayment(order, paymentInfo);
    return ApiResponse.success();
  }

  @PostMapping("/v1/payments/callback/success")
  public ApiResponse<Any> callbackPaymentSuccess(
      @RequestParam String orderId,
      @RequestParam String paymentKey,
      @RequestParam BigDecimal amount
  ) {
    paymentService.success(orderId, paymentKey, amount);
    return ApiResponse.success();
  }

  @PostMapping("/v1/payments/callback/fail")
  public ApiResponse<Any> callbackPaymentFail(
      @RequestParam String orderId,
      @RequestParam String code,
      @RequestParam String message
  ) {
    paymentService.fail(orderId, code, message);
    return ApiResponse.success();
  }
}
