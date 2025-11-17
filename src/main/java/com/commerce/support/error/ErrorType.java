package com.commerce.support.error;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;

public enum ErrorType {
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생 했습니다. 나중에 다시 시도해주세요."),

  PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),

  REVIEW_TARGET_TYPE_ERROR(HttpStatus.BAD_REQUEST, "잘못된 리뷰 작성 타입입니다."),
  REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
  REVIEW_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "리뷰를 이미 작성하셨습니다."),
  REVIEW_WRITE_DATE_EXPIRED(HttpStatus.BAD_REQUEST, "리뷰를 작성 가능한 기간이 넘었습니다. 리뷰를 작성하실 수 없습니다."),

  QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 질문 입니다."),
  QUESTION_WRITER_INCORRECT(HttpStatus.BAD_REQUEST, "작성자가 일치하지 않습니다."),

  FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 찜상품 입니다."),

  POINT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 포인트 계좌 입니다."),
  POINT_BALANCE_LOW(HttpStatus.INTERNAL_SERVER_ERROR, "포인트가 부족합니다."),
  POINT_BELOW_MINIMUM_THRESHOLD(HttpStatus.BAD_REQUEST, "포인트는 최소 1000원이 넘어야 사용이 가능합니다."),

  COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."),
  COUPON_ALREADY_DOWNLOADED(HttpStatus.BAD_REQUEST, "쿠폰이 이미 다운로드 되어 있습니다."),

  CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니에 해당 상품이 존재하지 않습니다."),
  CART_IS_EMPTY(HttpStatus.NOT_FOUND, "장바구니에 상품이 존재하지 않습니다."),

  OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "상품 재고가 존재하지 않습니다."),
  PRODUCT_MISMATCH_IN_ORDER(HttpStatus.BAD_REQUEST, "요청한 상품 정보와 일치하지 않습니다."),

  ORDER_NOT_PURCHASED(HttpStatus.BAD_REQUEST, "구매하지 않은 상품에 리뷰를 작성할 수 없습니다"),
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문 입니다."),
  ORDER_STATUS_NOT_PENDING(HttpStatus.BAD_REQUEST, "이미 처리된 주문입니다."),
  ORDER_USER_INCORRECT(HttpStatus.BAD_REQUEST, "주문자 정보가 일치하지 않습니다."),
  ORDER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 상품 정보가 존재하지 않습니다."),
  ORDER_CAN_NOT_CANCEL(HttpStatus.BAD_REQUEST, "주문을 취소할 수 없는 상태입니다.");

  private final HttpStatus status;
  private final String message;

  ErrorType(final HttpStatus status, final String message) {
    this.status = status;
    this.message = message;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}
