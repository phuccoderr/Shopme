package com.shopme.checkout;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.customer.CustomerService;
import com.shopme.order.OrderService;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;
import com.shopme.shippingrate.ShippingRateService;
import com.shopme.shoppingcart.ShoppingCartService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.List;

@Controller
public class CheckOutController {

    @Autowired private CheckOutService service;
    @Autowired private CustomerService customerService;
    @Autowired private AddressService addressService;
    @Autowired private ShippingRateService shipService;
    @Autowired private ShoppingCartService shoppingCartService;
    @Autowired private SettingService settingService;
    @Autowired private OrderService orderService;

    @GetMapping("/checkout")
    public String showCheckOutPage(Model model, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;

        if (defaultAddress != null) {
            model.addAttribute("shippingAddress",defaultAddress.toString());
            shippingRate = shipService.getShippingRateForAddress(defaultAddress);
        } else {
            model.addAttribute("shippingAddress",customer.getAddress());
            shippingRate = shipService.getShippingRateForCustomer(customer);
        }
        List<CartItem> listCartItems = shoppingCartService.listCartItems(customer);
        CheckOutInfo checkoutInfo = service.prepareCheckout(listCartItems, shippingRate);


        model.addAttribute("checkoutInfo",checkoutInfo);
        model.addAttribute("listCartItems",listCartItems);
        return "checkout/checkout";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String customer = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(customer);
    }

    @PostMapping("/place_order")
    public String placeOrder(Model model,HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String payment = request.getParameter("paymentMethod");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(payment);

        Customer customer = getAuthenticatedCustomer(request);

        Address address = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;
        if (address != null) {
            shippingRate = shipService.getShippingRateForAddress(address);
        } else {
            shippingRate = shipService.getShippingRateForCustomer(customer);
        }

        List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
        CheckOutInfo checkOutInfo = service.prepareCheckout(cartItems,shippingRate);

        Order createOrder = orderService.createOrder(customer,address,cartItems,paymentMethod,checkOutInfo);
        shoppingCartService.deleteByCustomer(customer);
        sendOrderConfirmationEmail(request,createOrder);

        return "checkout/order_completed";
    }

    private void sendOrderConfirmationEmail(HttpServletRequest request, Order createOrder) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettingBag = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBag);
        mailSender.setDefaultEncoding("utf-8");

        String toAddress = createOrder.getCustomer().getEmail();
        String subject = emailSettingBag.getOrderConfirmationSubject();
        String content = emailSettingBag.getOrderConfirmationContent();

        subject = subject.replace("[[orderId]]",String.valueOf(createOrder.getId()));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettingBag.getFromAddress(),emailSettingBag.getSenderName());
        helper.setSubject(subject);
        helper.setTo(toAddress);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss E, dd MM yyyy");
        String orderTime = dateFormat.format(createOrder.getOrderTime());

        CurrencySettingBag currencySettingBag = settingService.getCurrencySettings();
        String totalAmount = Utility.formatCurrency(createOrder.getTotal(),currencySettingBag);

        content = content.replace("[[name]]", createOrder.getCustomer().getFullName());
        content = content.replace("[[orderId]]",String.valueOf(createOrder.getId()));
        content = content.replace("[[orderTime]]",orderTime);
        content = content.replace("[[shippingAddress]]",createOrder.getDestination());
        content = content.replace("[[total]]", totalAmount);
        content = content.replace("[[paymentMethod]]", createOrder.getPaymentMethod().toString());

        helper.setText(content,true);
        mailSender.send(message);

    }
}
