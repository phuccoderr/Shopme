package com.shopme.shoppingcart;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem,Integer> {
    public List<CartItem> findByCustomer(Customer customer);

    public CartItem findByCustomerAndProduct(Customer customer, Product product);

    @Query("DELETE FROM CartItem c WHERE c.customer.id = ?1 AND c.product.id = ?2")
    @Modifying
    public void deleteByCustomerAndProduct(Integer Customer,Integer product);

    @Query("UPDATE CartItem c SET c.quantity = ?1 where c.customer.id = ?2 AND c.product.id = ?3")
    @Modifying
    public void updateQuantity(Integer quantity,Integer customer, Integer product);

    @Query("DELETE CartItem c WHERE c.customer.id = ?1")
    @Modifying
    public void deleteByCustomer(Integer customer);


}
