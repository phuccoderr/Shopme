package com.shopme.common.entity.product;

import com.shopme.common.IdBasedEntity;
import com.shopme.common.entity.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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

    public ProductDetail(String name,String value,Product product) {
        this.name = name;
        this.value = value;
        this.product = product;
    }

    public ProductDetail(Integer id, String name, String value, Product product) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.product = product;
    }
}
