package com.shopme.security;

import com.shopme.security.oauth.CustomerOAuth2UserService;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired private CustomerOAuth2UserService service;
    @Autowired private OAuth2LoginSuccessHandler auth2LoginSuccessHandler;
    @Autowired private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerDetailsService();
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    public void configure(AuthenticationManagerBuilder builder) {
        builder.authenticationProvider(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests( auth -> auth.requestMatchers("/account_detail").authenticated().anyRequest().permitAll()
                )
                .formLogin(
                        login -> login.loginPage("/login")
                                .usernameParameter("email")
                                .successHandler(databaseLoginSuccessHandler)
                                .permitAll()
                )
                .oauth2Login(
                        oauth -> oauth.loginPage("/login")
                                .userInfoEndpoint(
                                        userInfo -> userInfo.userService(service)
                                )
                                .successHandler(auth2LoginSuccessHandler)
                                .permitAll()
                )
                .logout(
                        logout -> logout.permitAll()
                ).rememberMe(
                        remember -> remember.key("abcdefg_01234567890")
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                );
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/css/**",
                "/fontawesome/**",
                "/webfonts/**",
                "/images/**",
                "/js/**",
                "/webjars/**");
        //tro vao cac file trong project
    }
}
