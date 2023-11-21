package com.shopme.admin.user.controller;

import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AccountController {
    @Autowired
    UserService service;

    @GetMapping("/account")
    public String viewAccount(@AuthenticationPrincipal ShopmeUserDetails loggedUser,Model model) {
        String email = loggedUser.getUsername();
        User user = service.getUserByEmail(email);
        List<Role> listRoles = service.listRoles();
        model.addAttribute("listRoles",listRoles);
        model.addAttribute("user",user);
        return "user/user_detail_modal";
    }
}
