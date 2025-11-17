package com.commerce.domain.product.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.domain.category.entity.Category;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("상품 - 카테고리 테스트")
class ProductCategoryTest {

  @Nested
  class 상품_카테고리_테스트 {

    @Test
    void 상품_카테고리_생성() {
      // given
      Product product = new Product("노트북", "설명", BigDecimal.valueOf(1000000), "url", "url html", 1000);
      Category category = new Category("전자제품");

      // when
      ProductCategory productCategory = new ProductCategory(product, category);

      // then
      assertThat(productCategory.getProduct().getId()).isEqualTo(product.getId());
      assertThat(productCategory.getCategory().getId()).isEqualTo(category.getId());
    }
  }
}