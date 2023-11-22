package com.shopme.admin.product;

import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;
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
    @Autowired private BrandService brandService;

    @GetMapping("/products")
    public String listAll(Model model) {
        List<Product> listProducts = service.listAll();
        model.addAttribute("listProducts",listProducts);
        return "product/product";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        Product product = new Product();
        List<Brand> listBrands = brandService.listBrand();
        model.addAttribute("listBrands",listBrands);
        model.addAttribute("product",product);

        Integer numberOfExistingExtraImages = product.getProductImages().size();
        model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
        return "product/product_form_modal";
    }

    @GetMapping("/products/detail/{id}")
    public String editProduct(Model model, @PathVariable(name = "id") Integer id) {
        Product product = service.get(id);
        List<Brand> listBrands = brandService.listBrand();
        model.addAttribute("listBrands",listBrands);
        model.addAttribute("product",product);

        Integer numberOfExistingExtraImages = product.getProductImages().size();
        model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
        return "product/product_form_modal";
    }
}
