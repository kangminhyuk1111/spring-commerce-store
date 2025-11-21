package com.commerce.domain.point.repository;

import com.commerce.domain.point.domain.Point;
import com.commerce.support.error.CoreException;
import com.commerce.support.error.ErrorType;
import com.commerce.utils.BaseReflectionUtils;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakePointRepository implements PointRepository {

  private final Map<Long, Point> store = new HashMap<>();
  private final AtomicLong idSequence = new AtomicLong(1);

  @Override
  public Optional<Point> findByUserId(Long userId) {
    return store.values().stream()
        .filter(point -> point.getUserId().equals(userId))
        .findFirst();
  }

  @Override
  public Point save(Point point) {
    if (point.getId() == null) {
      BaseReflectionUtils.setId(point, idSequence.getAndIncrement());
      BaseReflectionUtils.setCreatedAt(point, LocalDateTime.now());
      BaseReflectionUtils.setUpdatedAt(point, LocalDateTime.now());
    } else {
      BaseReflectionUtils.setUpdatedAt(point, LocalDateTime.now());
    }
    store.put(point.getId(), point);
    return point;
  }

  @Override
  public Point findByUserIdWithLock(Long userId) {
    return null;
  }

  public void clear() {
    store.clear();
    idSequence.set(1);
  }

  public int size() {
    return store.size();
  }

  public Point findById(Long id) {
    return store.get(id);
  }
}