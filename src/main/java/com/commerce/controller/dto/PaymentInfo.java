package com.commerce.controller.dto;

import com.commerce.support.error.CoreException;
import com.commerce.support.error.ErrorType;
import java.math.BigDecimal;

public record PaymentInfo(
    BigDecimal balance,
    BigDecimal usingPoint
) {
  public PaymentInfo {
    if (balance.compareTo(usingPoint) < 0) {
      throw new CoreException(ErrorType.POINT_BALANCE_LOW);
    }
  }
}
