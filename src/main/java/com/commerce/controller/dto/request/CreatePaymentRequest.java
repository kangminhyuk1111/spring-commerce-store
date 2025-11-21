package com.commerce.controller.dto.request;

import java.math.BigDecimal;

public record CreatePaymentRequest(
    String orderKey,
    BigDecimal usingPoint
) {
}
