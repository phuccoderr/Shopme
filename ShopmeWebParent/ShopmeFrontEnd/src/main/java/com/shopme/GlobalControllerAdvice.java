package com.shopme;

import com.shopme.category.CategoryService;
import com.shopme.common.Constants;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private CategoryService service;

    @ModelAttribute("listCategories")
    public List<Category> viewHomePage(Model model) {
        return service.listCategoryNoParent();
    }

    @ModelAttribute("S3_BASE_URI")
    public String awsCloud() {
        return Constants.S3_BASE_URI;
    }
}
