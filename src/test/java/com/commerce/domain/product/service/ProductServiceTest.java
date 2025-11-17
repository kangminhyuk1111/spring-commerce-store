package com.commerce.domain.product.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.domain.category.entity.Category;
import com.commerce.domain.product.entity.Product;
import com.commerce.domain.product.entity.ProductCategory;
import com.commerce.domain.product.repository.FakeProductCategoryRepository;
import com.commerce.domain.product.repository.FakeProductRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DisplayName("상품 서비스 테스트")
class ProductServiceTest {

  private Long categoryIdCounter = 1L;
  private ProductService productService;
  private FakeProductRepository fakeProductRepository;
  private FakeProductCategoryRepository fakeProductCategoryRepository;
  private PageRequest pageRequest;

  @BeforeEach
  void setUp() {
    fakeProductRepository = new FakeProductRepository();
    fakeProductCategoryRepository = new FakeProductCategoryRepository();
    productService = new ProductService(fakeProductRepository, fakeProductCategoryRepository);
    pageRequest = PageRequest.of(0, 10);
  }

  /*
  * ProductTest와 마찬가지로 상품 목록 조회가 1번 요구사항이므로 굳이 다른 기능(수정, 삭제, 단건 조회)은 아직 테스트 하지 않음
  * */
  @Nested
  class 상품_조회 {

    @BeforeEach
    void setUp() {
      final Product product1 = new Product("상품1", "상품1 입니다.", BigDecimal.valueOf(1000000), "http://localhost:33/1", "상품 디테일 설명 페이지", 10);
      fakeProductRepository.save(product1);
      final Product product2 = new Product("상품2", "상품2 입니다.", BigDecimal.valueOf(1100000), "http://localhost:33/2", "상품 디테일 설명 페이지", 10);
      fakeProductRepository.save(product2);
      final Product product3 = new Product("상품3", "상품3 입니다.", BigDecimal.valueOf(1200000), "http://localhost:33/3", "상품 디테일 설명 페이지", 10);
      fakeProductRepository.save(product3);

      final Category category1 = createCategory("카테고리1");
      final Category category2 = createCategory("카테고리2");

      fakeProductCategoryRepository.save(new ProductCategory(product1, category1));
      fakeProductCategoryRepository.save(new ProductCategory(product2, category1));
      fakeProductCategoryRepository.save(new ProductCategory(product3, category2));
    }

    @Test
    void 단일_카테고리_선택후_상품_목록_조회_테스트() {
      // given
      final List<Long> categoryIds = List.of(1L);

      // when
      final Page<Product> productsByCategoryIds = productService.findProductsByCategoryIds(categoryIds, pageRequest);

      // then
      assertThat(productsByCategoryIds.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 다중_카테고리_선택후_상품_목록_조회_테스트() {
      // given
      final List<Long> categoryIds = List.of(1L, 2L);

      // when
      final Page<Product> productsByCategoryIds = productService.findProductsByCategoryIds(categoryIds, pageRequest);

      // then
      assertThat(productsByCategoryIds.getTotalElements()).isEqualTo(3);
    }

    @Test
    void 카테고리_미선택시_전체_반환() {
      // given
      final List<Long> categoryIds = List.of();

      // when
      final Page<Product> productsByCategoryIds = productService.findProductsByCategoryIds(categoryIds, pageRequest);

      // then
      assertThat(productsByCategoryIds.getTotalElements()).isEqualTo(3);
    }

    @Test
    void 카테고리_선택시_일치하는_항목이_없는경우_빈리스트_반환() {
      // given
      final List<Long> categoryIds = List.of(3L);

      // when
      final Page<Product> productsByCategoryIds = productService.findProductsByCategoryIds(categoryIds, pageRequest);

      // then
      assertThat(productsByCategoryIds.isEmpty()).isTrue();
      assertThat(productsByCategoryIds.getTotalElements()).isEqualTo(0);
    }
  }

  private Category createCategory(String name) {
    Category category = new Category(name);
    setId(category, categoryIdCounter++);
    return category;
  }

  /*
  * category의 fakeRepository를 productService에서 의존하지 않기 때문에 임의로 테스트 클래스에서 setId를 통해 리플렉션으로 직접 id 주입
  * */
  private <T> void setId(T entity, Long id) {
    try {
      java.lang.reflect.Field field = entity.getClass().getSuperclass().getDeclaredField("id");
      field.setAccessible(true);
      field.set(entity, id);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}