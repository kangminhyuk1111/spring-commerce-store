package com.commerce.domain.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.commerce.domain.order.domain.NewOrder;
import com.commerce.domain.order.domain.NewOrderItem;
import com.commerce.domain.order.domain.Order;
import com.commerce.domain.order.domain.OrderStatus;
import com.commerce.domain.order.repository.FakeOrderRepository;
import com.commerce.domain.product.entity.Product;
import com.commerce.domain.product.repository.FakeProductCategoryRepository;
import com.commerce.domain.product.repository.FakeProductRepository;
import com.commerce.domain.product.service.ProductService;
import com.commerce.support.error.CoreException;
import com.commerce.support.error.ErrorType;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("주문 서비스")
class OrderServiceTest {

  private OrderService orderService;
  private FakeProductRepository productRepository;
  private FakeProductCategoryRepository productCategoryRepository;
  private FakeOrderRepository orderRepository;

  @BeforeEach
  void setUp() {
    productRepository = new FakeProductRepository();
    productCategoryRepository = new FakeProductCategoryRepository();
    orderRepository = new FakeOrderRepository();

    ProductService productService = new ProductService(productRepository, productCategoryRepository);

    setUpTestProducts(productRepository);

    orderService = new OrderService(
        productService,
        new OrderKeyGenerator(),
        orderRepository
    );

    productRepository.save(new Product());
  }

  @Nested
  class 주문을_생성한다 {

    @Test
    void 단건_주문이_성공적으로_생성된다() {
      // given
      Long userId = 1L;
      List<NewOrderItem> items = List.of(new NewOrderItem(1L, 5));
      NewOrder newOrder = new NewOrder(userId, items);

      // when
      String orderKey = orderService.createOrder(userId, newOrder);
      Order order = orderService.getOrder(userId, orderKey);

      // then
      assertAll(() -> {
        assertThat(order.getItems()).hasSize(1);
        assertThat(order.getItems().get(0).getId()).isEqualTo(1L);
      });
    }

    @Test
    void 다건_주문이_성공적으로_생성된다() {
      // given
      Long userId = 1L;
      List<NewOrderItem> items = List.of(new NewOrderItem(1L, 5), new NewOrderItem(2L, 5));
      NewOrder newOrder = new NewOrder(userId, items);

      // when
      String orderKey = orderService.createOrder(userId, newOrder);
      Order order = orderService.getOrder(userId, orderKey);

      // then
      assertAll(() -> {
        assertThat(order.getItems()).hasSize(2);
       });
    }

    @Test
    void 한명의_사용자가_여러주문을_생성_성공한다() {
      // given
      Long userId = 1L;
      List<NewOrderItem> items1 = List.of(new NewOrderItem(1L, 5), new NewOrderItem(2L, 5));
      List<NewOrderItem> items2 = List.of(new NewOrderItem(1L, 5), new NewOrderItem(2L, 5));
      NewOrder newOrder1 = new NewOrder(userId, items1);
      NewOrder newOrder2 = new NewOrder(userId, items2);
      String orderKey1 = orderService.createOrder(userId, newOrder1);
      String orderKey2 = orderService.createOrder(userId, newOrder2);

      // when
      List<Order> orders = orderService.getOrders(userId);

      // then
      assertAll(() -> {
        assertThat(orders.size()).isEqualTo(2);
      });
    }
  }

  @Nested
  class 주문을_실패한다 {

    @Test
    void 존재하지않는_상품에_대해서_주문을_하는경우_예외가_발생한다() {
      // given
      Long userId = 1L;
      List<NewOrderItem> items = List.of(new NewOrderItem(999L, 5));
      NewOrder newOrder = new NewOrder(userId, items);

      // when & then
      assertThatThrownBy(() -> orderService.createOrder(userId, newOrder))
          .isInstanceOf(CoreException.class)
          .hasMessage(ErrorType.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    void 주문한_상품_정보중_일부가_존재하지_않는_경우_예외가_발생한다() {
      // given
      Long userId = 1L;
      List<NewOrderItem> items = List.of(new NewOrderItem(1L, 5), new NewOrderItem(999L, 5));
      NewOrder newOrder = new NewOrder(userId, items);

      // when & then
      assertThatThrownBy(() -> orderService.createOrder(userId, newOrder))
          .isInstanceOf(CoreException.class)
          .hasMessage(ErrorType.PRODUCT_MISMATCH_IN_ORDER.getMessage());
    }

    @Test
    void 주문한_상품이_재고가_부족한_경우_예외가_발생한다() {
      // given
      Long userId = 1L;
      List<NewOrderItem> items = List.of(new NewOrderItem(1L, 999));
      NewOrder newOrder = new NewOrder(userId, items);

      // when & then
      assertThatThrownBy(() -> orderService.createOrder(userId, newOrder))
          .isInstanceOf(CoreException.class)
          .hasMessage(ErrorType.OUT_OF_STOCK.getMessage());
    }
  }

  @Nested
  class 주문을_조회한다 {

    @BeforeEach
    void setUp() {
      Long userId = 1L;
      List<NewOrderItem> items = List.of(new NewOrderItem(1L, 5), new NewOrderItem(2L, 5));
      NewOrder newOrder = new NewOrder(userId, items);
      String orderKey = orderService.createOrder(userId, newOrder);
    }

    @Test
    void 생성되어있는_주문을_조회에_성공한다() {
      // given
      Long userId = 1L;

      // when
      List<Order> orders = orderService.getOrders(userId);

      // then
      assertAll(() -> {
        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.get(0).getUserId()).isEqualTo(userId);
      });
    }

    @Test
    void 두개_이상_주문한_사용자의_주문목록을_조회에_성공한다() {
      // given
      Long userId = 1L;
      List<NewOrderItem> items = List.of(new NewOrderItem(3L, 5));
      NewOrder newOrder = new NewOrder(userId, items);
      orderService.createOrder(userId, newOrder);

      // when
      List<Order> orders = orderService.getOrders(userId);

      // then
      assertAll(() -> {
        assertThat(orders.size()).isEqualTo(2);
      });
    }

    @Test
    void 주문이_존재하지_않으면_예외를_발생시킨다() {
      // given
      Long userId = 999L;

      // when & then
      assertThatThrownBy(() -> orderService.getOrders(userId))
          .isInstanceOf(CoreException.class)
          .hasMessage(ErrorType.ORDER_NOT_FOUND.getMessage());
    }
  }

  @Nested
  class 주문_취소 {

    private String orderKey;

    @BeforeEach
    void setUp() {
      Long userId = 1L;
      List<NewOrderItem> items = List.of(new NewOrderItem(1L, 5), new NewOrderItem(2L, 5));
      NewOrder newOrder = new NewOrder(userId, items);
      orderKey = orderService.createOrder(userId, newOrder);
    }

    @Test
    void 주문을_취소가_성공할시_재고가_복구된다() {
      // given
      Long userId = 1L;
      Order order = orderService.getOrder(userId, orderKey);

      // when
      orderService.cancelOrder(orderKey);
      Order cancelledOrder = orderRepository.findByOrderKey(orderKey).get();

      // then
      assertAll(() -> {
        assertThat(cancelledOrder.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(productRepository.findById(1L).get().getStock()).isEqualTo(10);
        assertThat(productRepository.findById(2L).get().getStock()).isEqualTo(10);
      });
    }

    @Test
    void 주문_취소가_불가능한_상태인_경우_예외가_발생한다() {
      // given
      Long userId = 1L;
      Order order = orderService.getOrder(userId, orderKey);
      order.updateStatus(OrderStatus.SHIPPED);
      orderRepository.save(order);

      // when & then
      assertThatThrownBy(() -> orderService.cancelOrder(orderKey))
          .isInstanceOf(CoreException.class)
          .hasMessage(ErrorType.ORDER_CAN_NOT_CANCEL.getMessage());
    }
  }

  private void setUpTestProducts(FakeProductRepository productRepository) {
    final Product product1 = new Product("상품1", "상품1 입니다.", BigDecimal.valueOf(1000000), "http://localhost:33/1", "상품 디테일 설명 페이지", 10);
    productRepository.save(product1);
    final Product product2 = new Product("상품2", "상품2 입니다.", BigDecimal.valueOf(1100000), "http://localhost:33/2", "상품 디테일 설명 페이지", 10);
    productRepository.save(product2);
    final Product product3 = new Product("상품3", "상품3 입니다.", BigDecimal.valueOf(1200000), "http://localhost:33/3", "상품 디테일 설명 페이지", 10);
    productRepository.save(product3);
  }
}