package com.shopme.security.oauth;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired private CustomerService service;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomerOAuth2User oAuth2User = (CustomerOAuth2User) authentication.getPrincipal();
        String name = oAuth2User.getName();
        String email = oAuth2User.getEmail();
        String countryCode = request.getLocale().getCountry();
        String clientName = oAuth2User.getClientName();
        AuthenticationType authenticationType = getAuthenticationType(clientName);
        if (authenticationType.equals(AuthenticationType.GITHUB)) {
            email = oAuth2User.getLogin();
        }

        Customer customer = service.getCustomerByEmail(email);


        if (customer == null) {
            service.addNewCustomerUponOAuthLogin(name,email,countryCode,authenticationType);
        } else {
            oAuth2User.setFullName(customer.getFullName());
            service.updateAuthentication(customer, authenticationType);
            // truong hop dang ky trung email voi dang nhap email gg
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationType getAuthenticationType(String clientName) {
        if (clientName.equals("Google")) {
            return AuthenticationType.GOOGLE;
        } else if (clientName.equals("Facebook")) {
            return AuthenticationType.FACEBOOK;
        } else if (clientName.equals("GitHub")) {
            return AuthenticationType.GITHUB;
        } else {
            return AuthenticationType.DATABASE;
        }
    }
}
