package com.shopme.review.vote;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.ReviewVote;
import com.shopme.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ReviewVoteService {
    @Autowired private ReviewRepository reviewRepo;
    @Autowired private ReviewVoteRepository voteRepo;

    public VoteResult undoVote(ReviewVote vote,Integer reviewId, VoteType voteType) {
        voteRepo.delete(vote);
        reviewRepo.updateVoteCount(reviewId);
        Integer voteCount = reviewRepo.getVoteCount(reviewId);

        return VoteResult.success("You have unvoted " + voteType + " that review.",voteCount);
    }

    public VoteResult doVote(Integer reviewId, Customer customer, VoteType voteType) {
        Review review = null;
        try {
            review = reviewRepo.findById(reviewId).get();
        } catch (NoSuchElementException ex) {
            return VoteResult.fail("The reivew ID " + reviewId + " no longer exists.");
        }
        ReviewVote vote = voteRepo.findByReviewAndCustomer(reviewId,customer.getId());
        if (vote != null) {
            if (vote.isUpVoted() && voteType.equals(VoteType.UP) ||
                    vote.isDownVoted() && voteType.equals(VoteType.DOWN)) {
                return undoVote(vote,reviewId,voteType);
            } else if(vote.isUpVoted() && voteType.equals(VoteType.DOWN)) {
                vote.voteDown();
            } else if (vote.isDownVoted() && voteType.equals(VoteType.UP)) {
                vote.voteUp();
            }
        } else {
            vote = new ReviewVote();
            vote.setCustomer(customer);
            vote.setReview(review);

            if (voteType.equals(VoteType.UP)) {
                vote.voteUp();
            } else {
                vote.voteDown();
            }
        }
        voteRepo.save(vote);
        reviewRepo.updateVoteCount(reviewId);
        Integer voteCount = reviewRepo.getVoteCount(reviewId);
        return VoteResult.success("You have successfully voted " + voteType + " that reviews.",voteCount);
    }

    public void markReviewsVotedForProductByCustomer(List<Review> listReviews,Integer productId,Integer customerId) {
        List<ReviewVote> listVotes = voteRepo.findByProductAndCustomer(productId, customerId);
        for(ReviewVote vote : listVotes) {
            Review votedReview = vote.getReview();

            if (listReviews.contains(votedReview)) {
                int index = listReviews.indexOf(votedReview);
                Review review = listReviews.get(index);
                if (vote.isUpVoted()) {
                    review.setUpvotedByCurrentCustomer(true);
                    review.setDownvotedByCurrentCustomer(false);
                } else if (vote.isDownVoted()) {
                    review.setDownvotedByCurrentCustomer(true);
                    review.setUpvotedByCurrentCustomer(false);
                } else {
                    review.setUpvotedByCurrentCustomer(false);
                    review.setDownvotedByCurrentCustomer(false);
                }
            }
        }
    }
}
