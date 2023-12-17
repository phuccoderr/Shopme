package com.shopme;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;

import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.CurrencySettingBag;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.shopme.setting.EmailSettingBag;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public class Utility {

    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
        Object principal =  request.getUserPrincipal();
        if (principal == null) return null;

        String customerEmail = null;
        if (principal instanceof UsernamePasswordAuthenticationToken ||
                principal instanceof RememberMeAuthenticationToken) {
            customerEmail =request.getUserPrincipal().getName();
        } else if(principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) authenticationToken.getPrincipal();
            customerEmail = oAuth2User.getEmail();
            if (customerEmail == null) {
                customerEmail = oAuth2User.getLogin();
            }
        }
        return customerEmail;

    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        //cau hinh sever gmail
        mailSender.setHost(settings.getHost());
        mailSender.setPort(settings.getPort());
        mailSender.setUsername(settings.getUserName());
        mailSender.setPassword(settings.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", settings.getSmtpAuth());
        props.put("mail.smtp.starttls.enable", settings.getSmtpSecured());


        return mailSender;
    }

    public static String formatCurrency(float total, CurrencySettingBag currencySettingBag) {
        String symbol = currencySettingBag.getSymbol();
        String symbolPosition = currencySettingBag.getSymbolPositon();
        String decimalPointType = currencySettingBag.getDecimalPointType();
        String thousandPointType = currencySettingBag.getThousandsPointType();
        int decimalDigits = currencySettingBag.getDecimalDigits();


        String pattern = symbolPosition.equals("Before price") ? symbol : "";
        pattern += "###,###";
        if (decimalDigits > 0) {
            pattern += ".";
            for (int count = 1;count <= decimalDigits;count++) {
                pattern += "#";
            }
        }

        pattern += symbolPosition.equals("After price") ? symbol : "";

        char thousandSeparator = thousandPointType.equals("POINT") ? '.' : ',';
        char decimalSeparator = decimalPointType.equals("POINT") ? '.' : ',';

        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
        decimalFormatSymbols.setGroupingSeparator(thousandSeparator);

        DecimalFormat format = new DecimalFormat(pattern,decimalFormatSymbols);
        return format.format(total);

    }
}
