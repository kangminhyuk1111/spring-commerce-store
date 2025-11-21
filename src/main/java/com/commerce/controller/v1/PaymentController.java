package com.commerce.controller.v1;

import com.commerce.controller.dto.request.CreatePaymentRequest;
import com.commerce.domain.payment.application.PaymentService;
import com.commerce.support.response.ApiResponse;
import java.math.BigDecimal;
import org.hibernate.mapping.Any;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

  private final PaymentService paymentService;
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping("/v1/payment")
  public ApiResponse<Any> createPayment(Long userId, @RequestBody CreatePaymentRequest request) {
    paymentService.createPayment(userId, request.orderKey(), request.usingPoint());
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
