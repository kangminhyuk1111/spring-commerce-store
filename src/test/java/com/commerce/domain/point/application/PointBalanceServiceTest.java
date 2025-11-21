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

class PointBalanceServiceTest {

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
  class 포인트_계좌에 {

    @Test
    void 포인트가_추가되는_경우() {
      // given
      Long userId = 1L;
      BigDecimal amount = BigDecimal.valueOf(1000L);

      // when
      pointBalanceService.chargeBalance(userId, amount);

      // then
      assertThat(pointService.getPoint(userId).getBalance()).isEqualTo(amount);
    }

    @Test
    void 포인트가_사용되는_경우() {
      // given
      Long userId = 1L;
      BigDecimal amount = BigDecimal.valueOf(1000L);

      // when
      pointBalanceService.chargeBalance(userId, amount);
      pointBalanceService.useBalance(userId, amount);

      // then
      assertThat(pointService.getPoint(userId).getBalance()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void 포인트_사용시_포인트가_부족한_경우_예외가_발생한다() {
      // given
      Long userId = 1L;

      // when & then
      assertThatThrownBy(() -> pointBalanceService.useBalance(userId, BigDecimal.valueOf(999999L)))
          .isInstanceOf(CoreException.class)
          .hasMessage(ErrorType.POINT_BALANCE_LOW.getMessage());
    }
  }
}