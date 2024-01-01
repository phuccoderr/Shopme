package com.shopme.admin;

import com.shopme.common.Constants;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute("S3_BASE_URI")
    public String awsCloud() {
        return Constants.S3_BASE_URI;
    }
}
