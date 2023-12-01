package com.shopme.product;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {
    @Autowired private ProductService service;
    @Autowired private CategoryService cateService;

    @GetMapping("/c/{alias}")
    public String listProduct(@PathVariable(name = "alias") String alias, Model model) {
        Category category = cateService.getCategory(alias);
        List<Product> listProducts = service.listByCategory(category.getId());

        List<Category> listAll = cateService.listCategoryNoParent();

        model.addAttribute("listCategories",listAll);
        model.addAttribute("listProducts",listProducts);
        return "products/products";
    }
}
