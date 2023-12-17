package com.shopme.checkout;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckOutService {

    public CheckOutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {
        CheckOutInfo checkOutInfo = new CheckOutInfo();
        
        float productTotal = calculateProductTotal(cartItems);
        float shippingCost = 20000;
        float paymentTotal = productTotal + shippingCost;

        checkOutInfo.setProductTotal(productTotal);
        checkOutInfo.setShippingCostTotal(shippingCost);
        checkOutInfo.setPaymentTotal(paymentTotal);
        checkOutInfo.setDeliverDays(shippingRate.getDays());
        checkOutInfo.setCodSupported(shippingRate.isCodSupported());

        return checkOutInfo;
    }



    private float calculateProductTotal(List<CartItem> cartItems) {
        float total = 0.0f;
        for (CartItem item : cartItems) {
            total += item.getSubTotal();
        }
        return total;
    }
}
