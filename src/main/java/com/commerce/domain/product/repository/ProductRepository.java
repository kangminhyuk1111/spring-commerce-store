package com.commerce.domain.product.repository;

import com.commerce.domain.product.entity.Product;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {
  Page<Product> findAll(Pageable pageable);
  Page<Product> findByIdIn(Collection<Long> ids, Pageable pageable);
  List<Product> findAllById(Collection<Long> ids);
  Optional<Product> findById(Long productId);
}