package com.commerce.domain.product.repository;

import com.commerce.domain.product.entity.Product;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class FakeProductRepository implements ProductRepository {

  private final Map<Long, Product> store = new HashMap<>();
  private long sequence = 0L;

  @Override
  public Page<Product> findAll(Pageable pageable) {
    List<Product> products = new ArrayList<>(store.values());
    int start = (int) pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), products.size());

    if (start >= products.size()) {
      return new PageImpl<>(List.of(), pageable, products.size());
    }

    List<Product> pageContent = products.subList(start, end);
    return new PageImpl<>(pageContent, pageable, products.size());
  }

  @Override
  public Page<Product> findByIdIn(Collection<Long> ids, Pageable pageable) {
    List<Product> products = ids.stream()
        .map(store::get)
        .filter(p -> p != null)
        .toList();

    int start = (int) pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), products.size());

    if (start >= products.size()) {
      return new PageImpl<>(List.of(), pageable, products.size());
    }

    List<Product> pageContent = products.subList(start, end);
    return new PageImpl<>(pageContent, pageable, products.size());
  }

  @Override
  public List<Product> findAllById(Collection<Long> ids) {
    return ids.stream()
        .map(store::get)
        .filter(Objects::nonNull)
        .toList();
  }

  @Override
  public Optional<Product> findById(final Long id) {
    return Optional.ofNullable(store.get(id));
  }

  public <S extends Product> S save(S entity) {
    if (entity.getId() == null) {
      setId(entity, ++sequence);
    }
    store.put(entity.getId(), entity);
    return entity;
  }

  public void clear() {
    store.clear();
    sequence = 0L;
  }

  private <S extends Product> void setId(S entity, long id) {
    try {
      java.lang.reflect.Field field = entity.getClass().getSuperclass()
          .getDeclaredField("id");
      field.setAccessible(true);
      field.set(entity, id);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}