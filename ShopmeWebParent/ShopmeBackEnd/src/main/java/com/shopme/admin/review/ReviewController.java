package com.shopme.admin.review;
import com.shopme.admin.brand.BrandNotFoundException;
import com.shopme.common.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
@Controller
public class ReviewController {
    @Autowired private ReviewService service;


    @GetMapping("/reviews")
    public String listFirstPage(Model model) {
        return listByPage(1,"reviewTime","asc",null,model);
    }

    @GetMapping("/reviews/page/{pageNum}")
    private String listByPage(@PathVariable("pageNum") int pageNum, @Param("sortField") String sortField,
                              @Param("sortDir") String sortDir,
                              @Param("keyword") String keyword,
                              Model model) {
        Page<Review> page = service.listByPage(pageNum,sortField,sortDir,keyword);
        List<Review> listReviews = page.getContent();

        long startCount = (pageNum - 1) * service.REVIEW_PER_PAGE + 1;
        long endCount = startCount + service.REVIEW_PER_PAGE - 1;


        model.addAttribute("listReviews",listReviews);
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

        return "review/review";
    }


    @GetMapping("/reviews/detail/{id}")
    public String editProduct(Model model, @PathVariable(name = "id") Integer id, RedirectAttributes ra) {
        Review review = null;
        try {
            review = service.get(id);
            model.addAttribute("review",review);
            return "review/review_form_modal";
        } catch (ReviewNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
            return "review/review";
        }

    }

    @GetMapping("/reviews/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id")Integer id, RedirectAttributes ra) {
        try {
            service.delete(id);
            String message = "The review ID " + id + " has been deleted";
            ra.addFlashAttribute("message",message);
        } catch (ReviewNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
        }

        return "redirect:/brands";
    }
}
