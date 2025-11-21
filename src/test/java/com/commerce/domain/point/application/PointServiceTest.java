package com.commerce.domain.point.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.commerce.domain.point.domain.Point;
import com.commerce.domain.point.repository.FakePointHistoryRepository;
import com.commerce.domain.point.repository.FakePointRepository;
import com.commerce.domain.point.repository.PointHistoryRepository;
import com.commerce.domain.point.repository.PointRepository;
import com.commerce.support.error.CoreException;
import com.commerce.support.error.ErrorType;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PointServiceTest {

  private PointService pointService;
  private PointBalanceService pointBalanceService;

  @BeforeEach
  void setUp() {
    PointRepository pointRepository = new FakePointRepository();
    PointHistoryRepository pointHistoryRepository = new FakePointHistoryRepository();
    PointPolicyValidator pointPolicyValidator = new PointPolicyValidator();
    pointService = new PointService(pointRepository, pointHistoryRepository);
    pointBalanceService = new PointBalanceService(pointRepository, pointHistoryRepository, pointPolicyValidator);

    pointRepository.save(new Point(1L, BigDecimal.ZERO));
  }

  @Nested
  class 포인트_조회시 {

    @Test
    void 현재_금액을_반환한다() {
      // given
      Long userId = 1L;

      // when
      Point point = pointService.getPoint(userId);

      // then
      assertThat(point.getBalance()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void 계좌가_존재하지_않을_경우_예외를_반환한다() {
      // given & when & then
      assertThatThrownBy(() -> pointService.getPoint(999L))
          .isInstanceOf(CoreException.class)
          .hasMessage(ErrorType.POINT_NOT_FOUND.getMessage());
    }
  }
}