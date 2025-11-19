package com.commerce.domain.payment.application;

import com.commerce.domain.order.application.OrderKeyGenerator;
import com.commerce.domain.order.application.OrderService;
import com.commerce.domain.order.domain.Order;
import com.commerce.domain.order.domain.OrderItem;
import com.commerce.domain.order.domain.OrderStatus;
import com.commerce.domain.order.repository.FakeOrderRepository;
import com.commerce.domain.order.repository.OrderRepository;
import com.commerce.domain.payment.repository.FakePaymentRepository;
import com.commerce.domain.payment.repository.PaymentRepository;
import com.commerce.domain.point.application.PointService;
import com.commerce.domain.point.repository.FakePointHistoryRepository;
import com.commerce.domain.point.repository.FakePointRepository;
import com.commerce.domain.point.repository.PointHistoryRepository;
import com.commerce.domain.point.repository.PointRepository;
import com.commerce.domain.product.repository.FakeProductCategoryRepository;
import com.commerce.domain.product.repository.FakeProductRepository;
import com.commerce.domain.product.repository.ProductCategoryRepository;
import com.commerce.domain.product.repository.ProductRepository;
import com.commerce.domain.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PaymentServiceTest {

  private PaymentService paymentService;

  @BeforeEach
  void setUp() {
    ProductRepository productRepository = new FakeProductRepository();
    OrderRepository orderRepository = new FakeOrderRepository();
    PointHistoryRepository pointHistoryRepository = new FakePointHistoryRepository();
    PointRepository fakePointRepository = new FakePointRepository();
    PaymentRepository paymentRepository = new FakePaymentRepository();
    OrderKeyGenerator orderKeyGenerator = new OrderKeyGenerator();
    ProductCategoryRepository productCategoryRepository = new FakeProductCategoryRepository();

    ProductService productService = new ProductService(productRepository, productCategoryRepository);
    OrderService orderService = new OrderService(productService, orderKeyGenerator, orderRepository);
    PointService pointService = new PointService(fakePointRepository, pointHistoryRepository);

    paymentService = new PaymentService(paymentRepository, orderService, pointService);
  }

  @Nested
  class 결제_생성 {

    @Test
    void 결제를_생성한다() {
      // given

      // when

      // then
    }
  }
}