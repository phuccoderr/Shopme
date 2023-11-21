package com.shopme.admin.user.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {
    @Autowired private UserService service;


    @GetMapping("/users")
    public String listFirstPage(Model model) {
        return listByPage(1,"id","asc",null,model);
    }
    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             Model model) {
        Page<User> page = service.listByPage(pageNum,sortField,sortDir,keyword);
        List<User> listUsers = page.getContent();

        long startCount = (pageNum - 1) * service.USER_PER_PAGE + 1;
        long endCount = startCount + service.USER_PER_PAGE - 1;

        model.addAttribute("listUsers",listUsers);
        //pagination
        model.addAttribute("currentPage",pageNum); //page hien tai
        model.addAttribute("totalPages",page.getTotalPages()); //tong so trang
        model.addAttribute("startCount",startCount); //trang bat dau
        model.addAttribute("endCount",endCount); //trang ket thuc
        model.addAttribute("totalItems",page.getTotalElements());

        //param
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        return "user/user";
    }

    @GetMapping("/users/detail/{id}")
    public String editUserDetail(@PathVariable("id") Integer id, Model model,
                                 RedirectAttributes ra) {
        try {
            User user = service.get(id);
            List<Role> listRoles = service.listRoles();
            model.addAttribute("listRoles",listRoles);
            model.addAttribute("user",user);
            return "user/user_detail_modal";
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        User user = new User();
        user.setEnabled(true);
        List<Role> listRoles = service.listRoles();
        model.addAttribute("listRoles",listRoles);
        model.addAttribute("user",user);
        return "user/user_detail_modal";
    }

    @PostMapping ("/users/save")
    public String save(Model model, User user,
                       @RequestParam("fileImage")MultipartFile multipartFile,
                        RedirectAttributes ra) throws IOException {
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhoto(fileName);
            User savedUser = service.save(user);

            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        } else {
            service.save(user);
        }

        ra.addFlashAttribute("message","The user has been saved successfully.");
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateEnabledUser(@PathVariable(name = "id") Integer id,
                                    @PathVariable(name = "status") boolean enabled,
                                    RedirectAttributes ra) {
        service.updateEnabled(id,enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;
        ra.addFlashAttribute("message", message);

        return "redirect:/users";

    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,RedirectAttributes ra) {
        try {
            service.delete(id);
            String message = "The user ID " + id + " has been deleted";
            ra.addFlashAttribute("message",message);
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
        }

        return "redirect:/users";
    }
}
