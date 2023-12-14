package com.shopme.shoppingcart;

import com.shopme.Utility;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShoppingCartController {
    @Autowired private ShoppingCartService service;
    @Autowired private CustomerService customerService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = service.listCartItems(customer);

        float sum = 0;
        for (CartItem item : cartItems) {
            sum += item.getSubTotal();
        }

        int quantity = cartItems.size();
        model.addAttribute("quantity", quantity);

        model.addAttribute("sum",sum);
        model.addAttribute("cartItems", cartItems);

        return "cart/shopping_cart";

    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String customer = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(customer);
    }

}
