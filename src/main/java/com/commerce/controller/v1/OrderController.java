package com.commerce.controller.v1;

import com.commerce.controller.dto.request.CreateOrderFromCartRequest;
import com.commerce.controller.dto.request.CreateOrderRequest;
import com.commerce.controller.dto.response.CreateOrderResponse;
import com.commerce.controller.dto.response.OrderCheckoutResponse;
import com.commerce.domain.cart.application.CartService;
import com.commerce.domain.cart.domain.Cart;
import com.commerce.domain.coupon.application.CouponService;
import com.commerce.domain.order.application.OrderService;
import com.commerce.domain.order.domain.Order;
import com.commerce.domain.order.domain.OrderRecord;
import com.commerce.domain.order.domain.OrderStatus;
import com.commerce.domain.point.application.PointService;
import com.commerce.domain.point.domain.Point;
import com.commerce.support.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

  private final OrderService orderService;
  private final CartService cartService;
  private final PointService pointService;
  private final CouponService couponService;

  public OrderController(OrderService orderService, CartService cartService, PointService pointService, CouponService couponService) {
    this.orderService = orderService;
    this.cartService = cartService;
    this.pointService = pointService;
    this.couponService = couponService;
  }

  @PostMapping("/v1/orders")
  public ApiResponse<CreateOrderResponse> create(
      Long userId,
      @RequestBody CreateOrderRequest request
  ) {
    String orderKey = orderService.createOrder(
        userId,
        request.toNewOrder(userId)
    );

    return ApiResponse.success(CreateOrderResponse.of(orderKey));
  }

  @PostMapping("/v1/cart-orders")
  public ApiResponse<CreateOrderResponse> createOrderFromCart(
      Long userId,
      @RequestBody CreateOrderFromCartRequest request
  ) {
    Cart cart = cartService.getCart(userId);
    String orderKey = orderService.createOrder(
        userId,
        cart.toNewOrder(request.cartItemIds())
    );

    return ApiResponse.success(CreateOrderResponse.of(orderKey));
  }

  @GetMapping("/v1/orders/{orderKey}")
  public ApiResponse<OrderCheckoutResponse> findOrderForCheckout(
      Long userId,
      @PathVariable String orderKey
  ) {
    Order order = orderService.getOrder(userId, orderKey);
    Point point = pointService.findPoint(userId);

    return ApiResponse.success(OrderCheckoutResponse.of(order, point));
  }
}
