package com.commerce.domain.product.service;

import com.commerce.domain.product.entity.Product;
import com.commerce.domain.product.entity.ProductCategory;
import com.commerce.domain.product.repository.ProductCategoryRepository;
import com.commerce.domain.product.repository.ProductRepository;
import com.commerce.support.error.CoreException;
import com.commerce.support.error.ErrorType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductCategoryRepository productCategoryRepository;

  public ProductService(final ProductRepository productRepository,
      final ProductCategoryRepository productCategoryRepository) {
    this.productRepository = productRepository;
    this.productCategoryRepository = productCategoryRepository;
  }

  @Cacheable(
      cacheNames = "products",
      key = "T(String).format('%s-%s-%s', #categoryIds.stream().sorted().toList(), #pageable.offset, #pageable.pageSize)"
  )
  public Page<Product> findProductsByCategoryIds(final List<Long> categoryIds,
      final Pageable pageable) {
    if (categoryIds == null || categoryIds.isEmpty()) {
      return productRepository.findAll(pageable);
    }

    final List<ProductCategory> productCategories = productCategoryRepository.findByCategoryIdIn(
        categoryIds);

    List<Long> productIds = productCategories.stream()
        .map(productCategory -> productCategory.getProduct().getId())
        .distinct()
        .toList();

    if (productIds.isEmpty()) {
      return Page.empty(pageable);
    }

    return productRepository.findByIdIn(productIds, pageable);
  }

  public Product findProductById(final Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new CoreException(ErrorType.PRODUCT_NOT_FOUND));
  }

  public List<Product> findAllById(Collection<Long> orderProductIds) {
    return productRepository.findAllById(orderProductIds);
  }

  public void increaseStock(Long productId, Integer quantity) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CoreException(ErrorType.PRODUCT_NOT_FOUND));

    product.increaseStock(quantity);
  }
}
