package com.commerce.domain.order.application;

import com.commerce.domain.order.domain.NewOrder;
import com.commerce.domain.order.domain.NewOrderItem;
import com.commerce.domain.order.domain.Order;
import com.commerce.domain.order.domain.OrderItem;
import com.commerce.domain.order.domain.OrderStatus;
import com.commerce.domain.order.repository.OrderRepository;
import com.commerce.domain.product.entity.Product;
import com.commerce.domain.product.service.ProductService;
import com.commerce.support.error.CoreException;
import com.commerce.support.error.ErrorType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

  private final ProductService productService;
  private final OrderKeyGenerator orderKeyGenerator;
  private final OrderRepository orderRepository;

  public OrderService(ProductService productService, OrderKeyGenerator orderKeyGenerator,
      OrderRepository orderRepository) {
    this.productService = productService;
    this.orderKeyGenerator = orderKeyGenerator;
    this.orderRepository = orderRepository;
  }

  /*
   * 주문 생성
   * */
  @Transactional
  public String createOrder(Long userId, NewOrder newOrder) {
    Set<Long> orderProductIds = newOrder.items().stream()
        .map(NewOrderItem::productId)
        .collect(Collectors.toSet());

    Map<Long, Product> productsMap = productService.findAllById(orderProductIds).stream()
        .collect(Collectors.toMap(Product::getId, product -> product));

    validateCreateOrder(newOrder, productsMap, orderProductIds);

    BigDecimal totalPrice = calculateTotalPrice(newOrder, productsMap);

    List<OrderItem> orderItems = newOrder.items().stream()
        .map(item -> {
          Product product = productsMap.get(item.productId());
          return new OrderItem(
              product.getId(),
              null,
              product.getName(),
              product.getPrice(),
              item.quantity(),
              product.getPrice().multiply(BigDecimal.valueOf(item.quantity()))
          );
        })
        .toList();

    Order order = new Order(
        orderKeyGenerator.generate(userId),
        userId,
        totalPrice,
        OrderStatus.PENDING,
        orderItems
    );

    Order savedOrder = orderRepository.save(order);

    for (NewOrderItem item : newOrder.items()) {
      productsMap.get(item.productId()).decreaseStock(item.quantity());
    }

    return savedOrder.getOrderKey();
  }

  /*
   * 주문 조회
   * */
  public Order getOrder(Long userId, String orderKey) {
    Order order = orderRepository.findByOrderKey(orderKey)
        .orElseThrow(() -> new CoreException(ErrorType.ORDER_NOT_FOUND));

    if (!Objects.equals(order.getUserId(), userId)) {
      throw new CoreException(ErrorType.ORDER_USER_INCORRECT);
    }

    List<OrderItem> orderItems = orderRepository.findOrderItemsByOrderId(order.getId());

    if (orderItems.isEmpty()) {
      throw new CoreException(ErrorType.CART_ITEM_NOT_FOUND);
    }

    return new Order(
        order.getOrderKey(),
        userId,
        order.getTotalPrice(),
        order.getOrderStatus(),
        orderItems
    );
  }

  /*
   * 유저 ID로 주문 조회
   * */
  public List<Order> getOrders(Long userId) {
    List<Order> orders = orderRepository.findByUserId(userId);

    if (orders.isEmpty()) {
      throw new CoreException(ErrorType.ORDER_NOT_FOUND);
    }

    return orders;
  }

  /*
  * 주문 취소(상품 재고 복구)
  * */
  @Transactional
  public void cancelOrder(String orderKey) {
    Order order = orderRepository.findByOrderKey(orderKey)
        .orElseThrow(() -> new CoreException(ErrorType.ORDER_NOT_FOUND));

    if(!order.isCancellable()) {
      throw new CoreException(ErrorType.ORDER_CAN_NOT_CANCEL);
    }

    order.getItems().forEach(orderItem -> {
      productService.increaseStock(orderItem.getProductId(), orderItem.getQuantity());
    });

    order.cancel();
  }

  private BigDecimal calculateTotalPrice(NewOrder newOrder, Map<Long, Product> productsMap) {
    BigDecimal totalPrice = BigDecimal.ZERO;

    for (NewOrderItem item : newOrder.items()) {
      Product product = productsMap.get(item.productId());
      BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.quantity()));
      totalPrice = totalPrice.add(itemTotal);
    }

    return totalPrice;
  }

  private void validateCreateOrder(NewOrder newOrder, Map<Long, Product> productsMap,
      Set<Long> orderProductIds) {
    if (productsMap.isEmpty()) {
      throw new CoreException(ErrorType.PRODUCT_NOT_FOUND);
    }

    if (!productsMap.keySet().equals(orderProductIds)) {
      throw new CoreException(ErrorType.PRODUCT_MISMATCH_IN_ORDER);
    }

    for (NewOrderItem item : newOrder.items()) {
      Product product = productsMap.get(item.productId());

      if (product.getStock() < item.quantity()) {
        throw new CoreException(ErrorType.OUT_OF_STOCK);
      }
    }
  }
}
