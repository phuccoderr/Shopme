package com.shopme.customer;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    @Autowired private CustomerService service;
    @Autowired private SettingService settingService;

    @GetMapping("/forgot_password")
    public String showRequestForm() {
        return "customer/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processRequestForm(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        try {
            String token = service.updateResetPasswordToken(email);
            String link =  Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(link,email);
        } catch (CustomerNotFoundException e) {
            model.addAttribute("error",e.getMessage());
        } catch ( MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error","Cloud not send Email");
        }
        model.addAttribute("message","Vui lòng kiểm tra hộp thư email của bạn!");

        return "customer/forgot_password_form";
    }

    private void sendEmail(String link, String email) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettingBag = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBag);
        mailSender.setDefaultEncoding("utf-8");
        String toAddress = email;
        String subject = "Ấn vào link dưới đây để đặt lại mật khẩu";

        String content = "<p>Hello,</p>"
                + "<p>Bạn đã yêu cầu đặt lại mật khẩu.</p>"
                + "<p>Vui lòng nhấn link dưới đây để thay đổi password:</p>"
                + "<p><a href=\"" + link +  "\">Change my password</a></p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettingBag.getFromAddress(),emailSettingBag.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content,true);
        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetForm(@Param("token") String token,Model model) {
        Customer customer = service.getByResetPasswordToken(token);
        if (customer != null) {
            model.addAttribute("token",token);
        } else {
            model.addAttribute("message","Tài khoản của bạn không được tìm thấy");
            return "message";
        }
        return "customer/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetForm(HttpServletRequest request,Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        try {
            service.updatePassword(token,password);
            model.addAttribute("title","Đặt lại mật khẩu");
            model.addAttribute("message","Bạn đã đổi mật khẩu thành công.");
            return "message";
        } catch (CustomerNotFoundException e) {
            model.addAttribute("message",e.getMessage());
            return "message";
        }
    }
}
