package com.shopme.common.entity;

import com.shopme.common.IdBasedEntity;
import com.shopme.common.entity.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cartItems")
public class CartItem extends IdBasedEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;
    @Transient
    private float shippingCost;

    @Transient
    public float getSubTotal() {
        return product.getPrice() * quantity;
    }
}
