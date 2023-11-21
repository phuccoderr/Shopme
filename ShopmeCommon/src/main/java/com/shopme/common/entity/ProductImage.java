package com.shopme.common.entity;

import com.shopme.common.IdBasedEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage extends IdBasedEntity {

    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
