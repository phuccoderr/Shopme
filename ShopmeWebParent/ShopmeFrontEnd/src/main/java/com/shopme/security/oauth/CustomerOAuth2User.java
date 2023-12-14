package com.shopme.security.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomerOAuth2User implements OAuth2User {
    private String clientName;
    private String fullName;
    private OAuth2User oAuth2User;

    public CustomerOAuth2User(OAuth2User oAuth2User,String clientName) {
        this.oAuth2User = oAuth2User;
        this.clientName = clientName;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }

    public String getLogin() {
        return oAuth2User.getAttribute("login");
    }



    public String getClientName() {
        return clientName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getFullName() {
        return fullName;
    }

}
