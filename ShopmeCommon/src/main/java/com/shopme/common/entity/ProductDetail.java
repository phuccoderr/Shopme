package com.shopme.common.entity;

import com.shopme.common.IdBasedEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "product_details")
public class ProductDetail extends IdBasedEntity {
    @Column(nullable = false, length = 255)
    private String name;
    @Column(nullable = false, length = 255)
    private String value;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
