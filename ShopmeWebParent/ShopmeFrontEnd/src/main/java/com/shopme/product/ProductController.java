package com.shopme.product;

import com.shopme.brand.BrandService;
import com.shopme.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {
    @Autowired private ProductService service;
    @Autowired private CategoryService cateService;
    @Autowired private BrandService brandService;

    @GetMapping("/c/{alias}")
    public String viewCategoryFirstPage(@PathVariable(name = "alias") String alias,Model model) {
        return listProduct(alias,1,null,null,model);
    }

    @GetMapping("/c/{alias}/page/{pageNum}")
    public String listProduct(@PathVariable(name = "alias") String alias,
                              @PathVariable(name = "pageNum")Integer pageNum,
                              @Param("keyword") String keyword,
                              @Param("brandName") String brandName,
                              Model model) {
        Category category = cateService.getCategory(alias);
        Brand brand = brandService.getByName(brandName);

        Page<Product> pageProducts = service.listByCategory(pageNum,category.getId(),brand,keyword);
        List<Product> listProducts = pageProducts.getContent();
        List<Category> listCategories = cateService.listCategoryNoParent();

        //pagination
        long startCount = (pageNum - 1) * service.PRODUCTS_PER_PAGE + 1;
        long endCount = startCount + service.PRODUCTS_PER_PAGE - 1;

        if (endCount > pageProducts.getTotalElements()) {
            endCount = pageProducts.getTotalElements();
        }

        model.addAttribute("totalItems",pageProducts.getTotalElements());
        model.addAttribute("totalPages",pageProducts.getTotalPages());
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("endCount",endCount);
        model.addAttribute("startCount",startCount);
        model.addAttribute("listProducts",listProducts);
        model.addAttribute("category",category);

        model.addAttribute("category",category);
        model.addAttribute("listCategories",listCategories);
        model.addAttribute("listProducts",listProducts);
        return "products/products";
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable(name = "product_alias")String alias,Model model) {
        Product product = service.getProduct(alias);
        List<Category> listCategoryParents = cateService.getCategoryParents(product.getCategory());

        model.addAttribute("listCategoryParents",listCategoryParents);
        model.addAttribute("product",product);
        return "products/product_detail";
    }


}
