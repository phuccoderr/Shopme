package com.shopme.customer;


import com.shopme.Utility;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.security.CustomerDetails;
import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired private CustomerService service;
    @Autowired private SettingService settingService;



    @GetMapping("/register")
    public String registerCustomer(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer",customer);
        return "register/register";
    }

    @PostMapping("/create_customer")
    public String createCustomer(Customer customer, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        service.register(customer);
        sendVerificationEmail(request,customer);
        return "register/verify_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettingBag = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBag);
        mailSender.setDefaultEncoding("utf-8");

        String toAddress = customer.getEmail();
        String subject = emailSettingBag.getCustomerVerirySubject();
        String content = emailSettingBag.getCustomerVeriryContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettingBag.getFromAddress(),emailSettingBag.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", customer.getFullName());
        //lay url xac thuc
        String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();

        content = content.replace("[[URL]]",verifyURL);

        helper.setText(content,true);
        mailSender.send(message);
    }
    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, Model model) {
        boolean verified = service.verify(code);

        return "register/" + (verified ? "register_success" : "verify_fail");
    }

    @GetMapping("/account_detail")
    public String viewCustomer(Model model, HttpServletRequest request) {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        Customer customer = service.getCustomerByEmail(email);
        List<Country> listCountries = service.listCountries();

        model.addAttribute("listCountries",listCountries);
        model.addAttribute("customer",customer);
        return "customer/account_form";
    }




    @PostMapping("/update_account_detail")
    public String saveCustomer(Customer customer, RedirectAttributes ra, HttpServletRequest request) {
        service.update(customer);
        ra.addFlashAttribute("message", "Your account details have been updated.");
        updateNameAuthenticatedCustomer(customer,request);

        String redirect = request.getParameter("redirect");
        if (redirect.equals("address_book")) {
            return "redirect:/address";
        }

        return "redirect:/account_detail";
    }

    private void updateNameAuthenticatedCustomer(Customer customer,HttpServletRequest request) {
        Object principal = request.getUserPrincipal();
        if (principal instanceof UsernamePasswordAuthenticationToken ||
                    principal instanceof RememberMeAuthenticationToken) {
            CustomerDetails details = getCustomerUserDetails(principal);
            Customer authenCustomer = details.getCustomer();
            authenCustomer.setFirstName(customer.getFirstName());
            authenCustomer.setLastName(customer.getLastName());
        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) token.getPrincipal();
            String fullName = customer.getFullName();
            oAuth2User.setFullName(customer.getFullName());
        }
    }

    private CustomerDetails getCustomerUserDetails(Object principal) {
        CustomerDetails userDetails = null;
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            userDetails = (CustomerDetails) token.getPrincipal();
        } else if (principal instanceof  RememberMeAuthenticationToken) {
            RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
            userDetails = (CustomerDetails) token.getPrincipal();
        }
        return userDetails;
    }

}
