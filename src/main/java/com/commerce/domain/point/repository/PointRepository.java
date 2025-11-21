package com.commerce.domain.point.repository;

import com.commerce.domain.point.domain.Point;
import java.util.Optional;

public interface PointRepository {
  Optional<Point> findByUserId(Long userId);
  Point save(Point point);
  Point findByUserIdWithLock(Long userId);
}
