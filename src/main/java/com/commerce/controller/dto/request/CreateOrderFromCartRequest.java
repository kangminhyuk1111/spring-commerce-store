package com.commerce.controller.dto.request;

import java.util.List;

public record CreateOrderFromCartRequest(
    List<Long> cartItemIds
) {
}
