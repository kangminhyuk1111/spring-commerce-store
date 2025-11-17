package com.commerce.domain.product.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("상품 테스트")
class ProductTest {

  /*
  * 1번은 상품 목록조회 이기 때문에 상품 도메인은 최소한의 생성자를 통한 생성 로직만 테스트함
  * 삭제, 상태변경 같은 테스트는 추후 과제
  * 추후 생성시에 length check나 not null 같은 부분이 필요하다면 그 때 테스트 코드를 작성하는게 좋다고 생각
  * 당장 필요하지 않은 메서드나 테스트를 굳이 미리 작성할 필요는 없다고 생각
  * */
  @Nested
  class 상품_생성 {

    @Test
    void 상품_생성_테스트() {
      // given
      final String name = "노트북";
      final String description = "신규 발매한 노트북 입니다.";
      final BigDecimal price = BigDecimal.valueOf(1000000);
      final String imagePathUrl = "http://test-image-path:8080";
      final String detailedDescription = "발매한 노트북의 상세 설명 페이지 입니다.";
      final Integer stock = 1000;

      // when
      final Product product = new Product(name, description, price, imagePathUrl, detailedDescription, stock);

      // then
      assertThat(product.getId()).isNull();
      assertThat(product.getName()).isEqualTo(name);
      assertThat(product.getDescription()).isEqualTo(description);
      assertThat(product.getPrice()).isEqualTo(price);
      assertThat(product.getImagePathUrl()).isEqualTo(imagePathUrl);
    }
  }
}