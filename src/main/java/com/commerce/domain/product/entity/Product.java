package com.commerce.domain.product.entity;

import com.commerce.support.entity.BaseEntity;
import com.commerce.support.error.CoreException;
import com.commerce.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private String imagePathUrl;

  private String detailedDescription;

  @Column(nullable = false)
  private Integer stock;

  public Product() {
  }

  public Product(final String name, final String description, final BigDecimal price, final String imagePathUrl, final String detailedDescription, final Integer stock) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.imagePathUrl = imagePathUrl;
    this.detailedDescription = detailedDescription;
    this.stock = stock;
  }

  public void decreaseStock(Integer quantity) {
    if(this.stock < quantity) {
      throw new CoreException(ErrorType.OUT_OF_STOCK);
    }
    this.stock = this.stock - quantity;
  }

  public void increaseStock(Integer quantity) {
    this.stock = this.stock + quantity;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public String getImagePathUrl() {
    return imagePathUrl;
  }

  public String getDetailedDescription() {
    return detailedDescription;
  }

  public Integer getStock() {
    return stock;
  }
}
