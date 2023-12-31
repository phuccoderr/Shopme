package com.shopme.shoppingcart;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;
import com.shopme.customer.CustomerService;
import com.shopme.shippingrate.ShippingRateService;
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
    @Autowired private AddressService addressService;
    @Autowired private ShippingRateService shippingRateService;


    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = service.listCartItems(customer);

        float sum = 0;
        for (CartItem item : cartItems) {
            sum += item.getSubTotal();
        }


        Address address = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;
        boolean usePrimaryAddressAsDefault = false;

        if (address != null) {
            shippingRate = shippingRateService.getShippingRateForAddress(address);
        } else {
            usePrimaryAddressAsDefault = true;
            shippingRate = shippingRateService.getShippingRateForCustomer(customer);
        }

        model.addAttribute("usePrimaryAddressAsDefault",usePrimaryAddressAsDefault);
        model.addAttribute("shippingRateSupported",shippingRate != null);


        int quantity = cartItems.size();
        float shippingCost = 0;
        if (shippingRate != null) {
            for (CartItem item :  cartItems) {
                shippingCost += (shippingRate.getRate() * item.getQuantity());
            }
        }
        model.addAttribute("quantity", quantity);
        model.addAttribute("shippingCost",shippingCost);
        model.addAttribute("sum",sum);
        model.addAttribute("cartItems", cartItems);

        return "cart/shopping_cart";

    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String customer = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(customer);
    }

}
