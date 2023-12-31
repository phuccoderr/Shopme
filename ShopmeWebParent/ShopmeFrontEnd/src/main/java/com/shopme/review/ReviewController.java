package com.shopme.review;

import com.shopme.ControllerHelper;
import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.product.Product;
import com.shopme.customer.CustomerService;
import com.shopme.product.ProductNotFoundException;
import com.shopme.product.ProductService;
import com.shopme.review.vote.ReviewVoteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ReviewController {
    @Autowired private ReviewService service;
    @Autowired private ProductService productService;
    @Autowired private ControllerHelper controllerHelper;
    @Autowired private ReviewVoteService voteService;
    @GetMapping("/reviews")
    public String listFirstPage(Model model,HttpServletRequest request) {
        return listByPage(1,"reviewTime","asc",null,request,model);
    }

    @GetMapping("/reviews/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             @Param("sortField") String sortField,
                             @Param("sortDir")String sortDir,
                             @Param("keyword")String keyword,
                             HttpServletRequest request,
                             Model model) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        Customer customer = controllerHelper.getAuthenticationCustomer(request);
        Page<Review> pageReview = service.listByCustomerByPage(pageNum, sortField, sortDir, keyword, customer.getId());
        List<Review> listReviews = pageReview.getContent();

        model.addAttribute("listReviews",listReviews);
        //pagination
        long startCount = (pageNum - 1) * service.REVIEW_PER_PAGE + 1;
        long endCount = startCount + service.REVIEW_PER_PAGE - 1;

        if (endCount > pageReview.getTotalElements()) {
            endCount = pageReview.getTotalElements();
        }

        model.addAttribute("totalItems",pageReview.getTotalElements());
        model.addAttribute("totalPages",pageReview.getTotalPages());
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("endCount",endCount);
        model.addAttribute("startCount",startCount);

        model.addAttribute("keyword",keyword);
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",reverseSortDir);
        return "review/review";
    }


    @GetMapping("/reviews/detail/{id}")
    public String detailReview(@PathVariable("id") Integer id,Model model,HttpServletRequest request) {
        Customer customer = controllerHelper.getAuthenticationCustomer(request);

        Review review = service.getReviewByCustomer(customer.getId(),id);

        model.addAttribute("review",review);

        return "review/review_form_modal";
    }
    @GetMapping("/ratings/{productAlias}")
    public String listFirstPage(@PathVariable(name = "productAlias") String productAlias,
                                Model model,HttpServletRequest request) {
        return listByProductByPage(productAlias,1,"votes","desc",request,model);
    }

    @GetMapping("/ratings/{productAlias}/page/{pageNum}")
    public String listByProductByPage(@PathVariable(name = "productAlias") String productAlias,
                                      @PathVariable(name = "pageNum") int pageNum,
                             @Param("sortField") String sortField,
                             @Param("sortDir")String sortDir,
                             HttpServletRequest request,
                             Model model) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        Product product = null;
        try {
            product = productService.getProduct(productAlias);
        } catch (ProductNotFoundException e) {
            return "error/404";
        }
        Page<Review> pageReview = service.listByProduct(product, pageNum, sortField, sortDir);
        List<Review> listReviews = pageReview.getContent();
        Customer customer = controllerHelper.getAuthenticationCustomer(request);
        if (customer != null) {
            voteService.markReviewsVotedForProductByCustomer(listReviews,product.getId(),customer.getId());
        }

        model.addAttribute("listReviews",listReviews);
        model.addAttribute("product",product);
        //pagination
        long startCount = (pageNum - 1) * service.REVIEW_PER_PAGE + 1;
        long endCount = startCount + service.REVIEW_PER_PAGE - 1;

        if (endCount > pageReview.getTotalElements()) {
            endCount = pageReview.getTotalElements();
        }

        model.addAttribute("totalItems",pageReview.getTotalElements());
        model.addAttribute("totalPages",pageReview.getTotalPages());
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("endCount",endCount);
        model.addAttribute("startCount",startCount);

        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",reverseSortDir);
        return "review/review_product";
    }

    @GetMapping("/write_review/product/{id}")
    public String showViewForm(@PathVariable("id") Integer id,
                               Model model,
                               HttpServletRequest request) {
        Review review = new Review();

        try {
            Product product = productService.getProduct(id);
            Customer customer = controllerHelper.getAuthenticationCustomer(request);
            if (customer != null) {
                boolean customerReviewd = service.didCustomerReviewProduct(customer, product.getId());

                if(customerReviewd) {
                    model.addAttribute("customerReviewed",customerReviewd);
                } else {
                    boolean customerCanReview = service.canCustomerReviewProduct(customer, product.getId());
                    if(customerCanReview) {
                        model.addAttribute("customerCanReview",customerCanReview);
                    } else {
                        model.addAttribute("NoReviewPermission",true);
                    }

                }
            }
            model.addAttribute("product",product);
        } catch (ProductNotFoundException e) {
            return "error/404";
        }

        model.addAttribute("review",review);
        return "review/review_form";
    }


    @PostMapping("/post_review")
    public String saveReview(Model model,Review review,Integer productId,HttpServletRequest request) {
        Customer customer = controllerHelper.getAuthenticationCustomer(request);
        try {
            Product product = productService.getProduct(productId);
            review.setProduct(product);
            review.setCustomer(customer);
            Review saved = service.save(review);


            model.addAttribute("review",saved);
        } catch (ProductNotFoundException e) {
            return "error/404";
        }


        return "review/review_done";
    }



}
