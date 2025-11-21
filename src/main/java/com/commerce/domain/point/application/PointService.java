package com.commerce.domain.point.application;

import com.commerce.domain.point.domain.Point;
import com.commerce.domain.point.domain.PointHistory;
import com.commerce.domain.point.repository.PointHistoryRepository;
import com.commerce.domain.point.repository.PointRepository;
import com.commerce.support.error.CoreException;
import com.commerce.support.error.ErrorType;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PointService {

  private final PointRepository pointRepository;
  private final PointHistoryRepository pointHistoryRepository;

  public PointService(PointRepository pointRepository, PointHistoryRepository pointHistoryRepository) {
    this.pointRepository = pointRepository;
    this.pointHistoryRepository = pointHistoryRepository;
  }

  // 현재 포인트 검색
  public Point getPoint(Long userId) {
    return pointRepository.findByUserId(userId)
        .orElseThrow(() -> new CoreException(ErrorType.POINT_NOT_FOUND));
  }

  // 유저의 포인트 계좌 유동 내역 조회(시간순 정렬)
  public List<PointHistory> findHistories(Long userId) {
    return pointHistoryRepository.findByUserIdOrderByTransactionAtDesc(userId);
  }

  public void usePoint(Long userId, BigDecimal usingPoint) {
    Point point = pointRepository.findByUserId(userId)
        .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND_DATA));

    point.use(usingPoint);
  }
}
