package com.shopme.common.entity.order;

import com.shopme.common.IdBasedEntity;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "order_details")
public class OrderDetail extends IdBasedEntity {
    private int quantity;
    private float shippingCost;
    private float productCost;
    private float unitPrice;
    private float subtotal;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderDetail(String categoryName,int quantity,float productCost, float shippingCost, float subtotal) {
        this.product = new Product();
        this.product.setCategory(new Category(categoryName));
        this.quantity = quantity;
        this.productCost = productCost;
        this.shippingCost = shippingCost;
        this.subtotal = subtotal;
    }

    public OrderDetail(int quantity,String productName,float productCost, float shippingCost, float subtotal) {
        this.quantity = quantity;
        this.product = new Product(productName);
        this.productCost = productCost;
        this.shippingCost = shippingCost;
        this.subtotal = subtotal;
    }
}
