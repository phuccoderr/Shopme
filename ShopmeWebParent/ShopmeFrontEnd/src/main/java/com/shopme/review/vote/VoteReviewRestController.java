package com.shopme.review.vote;

import com.shopme.ControllerHelper;
import com.shopme.common.entity.Customer;
import com.shopme.review.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteReviewRestController {
    @Autowired private ReviewVoteService service;
    @Autowired private ControllerHelper controllerHelper;

    @PostMapping("/vote_review/{id}/{type}")
    public VoteResult voteReview(@PathVariable("id") Integer reviewId,
                                 @PathVariable("type") String type,
                                 HttpServletRequest request) {
        Customer customer = controllerHelper.getAuthenticationCustomer(request);

        if (customer == null) {
            return VoteResult.fail("Bạn cần phải đăng nhập để có thể tương tác!");
        }
        VoteType voteType = VoteType.valueOf(type.toUpperCase());
        return service.doVote(reviewId,customer,voteType);
    }
}
