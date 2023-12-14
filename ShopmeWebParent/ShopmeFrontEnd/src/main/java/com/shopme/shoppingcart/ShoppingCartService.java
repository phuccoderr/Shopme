package com.shopme.shoppingcart;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import com.shopme.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShoppingCartService {
    @Autowired private CartItemRepository repo;
    @Autowired private ProductRepository productRepo;

    public Integer addProduct(Integer productId,Integer quantity, Customer customer) throws ShoppingCartException {
        Product product = productRepo.findById(productId).get();

        Integer updateQuantity = quantity;

        CartItem cartItem = repo.findByCustomerAndProduct(customer,product);

        if (cartItem != null) {
            updateQuantity = cartItem.getQuantity() + quantity;
            if (updateQuantity > 5) {
                throw new ShoppingCartException("Could not add more " + quantity + " item(s)"
                        + "because there's already " + cartItem.getQuantity() + " item(s) "
                        + "in your shopping cart. Maximum allowed quantity is 5.");
            }
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCustomer(customer);
        }

        cartItem.setQuantity(updateQuantity);
        repo.save(cartItem);
        return updateQuantity;

    }

    public List<CartItem> listCartItems(Customer customer) {
        return repo.findByCustomer(customer);
    }

    public void removeProduct(Integer customerId,Integer productId) {
        repo.deleteByCustomerAndProduct(customerId,productId);
    }

    public float updateQuantity(Integer productId, Integer quantity, Customer customer) {
        repo.updateQuantity(quantity,customer.getId(),productId);
        Product product = productRepo.findById(productId).get();
        float subtotal = product.getDiscountPrice() * quantity;
        return subtotal;
    }
}
