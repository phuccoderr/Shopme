package com.shopme.product;

import com.shopme.ControllerHelper;
import com.shopme.Utility;
import com.shopme.brand.BrandService;
import com.shopme.category.CategoryService;
import com.shopme.common.entity.*;
import com.shopme.common.entity.product.Product;
import com.shopme.customer.CustomerService;
import com.shopme.review.ReviewService;
import com.shopme.review.vote.ReviewVoteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {
    @Autowired private ProductService service;
    @Autowired private CategoryService cateService;
    @Autowired private BrandService brandService;
    @Autowired private ReviewService reviewService;
    @Autowired private ReviewVoteService voteService;
    @Autowired private ControllerHelper controllerHelper;

    @GetMapping("/c/{alias}")
    public String viewCategoryFirstPage(@PathVariable(name = "alias") String alias,Model model) {
        return listProduct(alias,1,null,null,null,model);
    }

    @GetMapping("/c/{alias}/page/{pageNum}")
    public String listProduct(@PathVariable(name = "alias") String alias,
                              @PathVariable(name = "pageNum")Integer pageNum,
                              @Param("keyword") String keyword,
                              @Param("selectedBrand") String selectedBrand,
                              @Param("sortField") String sortField,
                              Model model) {


        Category category = cateService.getCategory(alias);
        Brand brand = brandService.getByName(selectedBrand);
        Page<Product> pageProducts = service.listByCategory(pageNum,category.getId(),brand,sortField,keyword);
        List<Product> listProducts = pageProducts.getContent();
        List<Category> listCategoryParents = cateService.getCategoryParents(category);

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

        model.addAttribute("selectedBrand",selectedBrand);
        model.addAttribute("keyword",keyword);
        model.addAttribute("sortField",sortField);

        model.addAttribute("listCategoryParents",listCategoryParents);
        return "products/products";
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable(name = "product_alias")String alias,Model model,
                                    HttpServletRequest request) {
        Product product = null;
        try {
            product = service.getProduct(alias);
            List<Category> listCategoryParents = cateService.getCategoryParents(product.getCategory());
            Page<Review> listReviews = reviewService.listByProduct(product, 1, "votes", "desc");
            Customer customer = controllerHelper.getAuthenticationCustomer(request);
            if (customer != null) {
                boolean customerReviewd = reviewService.didCustomerReviewProduct(customer, product.getId());
                voteService.markReviewsVotedForProductByCustomer(listReviews.getContent(), product.getId(), customer.getId());
                if(customerReviewd) {
                    model.addAttribute("customerReviewed",customerReviewd);
                } else {
                    boolean customerCanReview = reviewService.canCustomerReviewProduct(customer, product.getId());
                    model.addAttribute("customerCanReview",customerCanReview);
                }
            }

            model.addAttribute("listCategoryParents",listCategoryParents);
            model.addAttribute("listReviews",listReviews.getContent());
            model.addAttribute("product",product);
            return "products/product_detail";
        } catch (ProductNotFoundException e) {
            return "error/404";
        }

    }









}
