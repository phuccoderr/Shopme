package com.shopme.checkout;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckOutService {

    public CheckOutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {
        CheckOutInfo checkOutInfo = new CheckOutInfo();

        float productCost = calculateProductCost(cartItems);
        float productTotal = calculateProductTotal(cartItems);
        float shippingCost = calculateShippingCost(cartItems,shippingRate);
        float paymentTotal = productTotal + shippingCost;

        checkOutInfo.setProductCost(productCost);
        checkOutInfo.setProductTotal(productTotal);
        checkOutInfo.setShippingCostTotal(shippingCost);
        checkOutInfo.setPaymentTotal(paymentTotal);
        checkOutInfo.setDeliverDays(shippingRate.getDays());
        checkOutInfo.setCodSupported(shippingRate.isCodSupported());

        return checkOutInfo;
    }

    private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRate) {
        float shippingCostTotal = 0.0f;

        for (CartItem item : cartItems) {
            float shippingCost = item.getQuantity() * shippingRate.getRate();

            item.setShippingCost(shippingCost);

            shippingCostTotal += shippingCost;
        }

        return shippingCostTotal;
    }



    private float calculateProductTotal(List<CartItem> cartItems) {
        float total = 0.0f;
        for (CartItem item : cartItems) {
            total += item.getSubTotal();
        }
        return total;
    }

    private float calculateProductCost(List<CartItem> cartItems) {
        float cost = 0.0f;
        for (CartItem item : cartItems) {
            cost += item.getQuantity() * item.getProduct().getCost();
        }
        return cost;
    }
}
