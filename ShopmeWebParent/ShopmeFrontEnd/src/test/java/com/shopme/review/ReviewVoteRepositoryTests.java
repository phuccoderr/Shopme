package com.shopme.review;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.ReviewVote;
import com.shopme.review.vote.ReviewVoteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ReviewVoteRepositoryTests {
    @Autowired private ReviewVoteRepository repo;

    @Test
    public void testSaveVote() {
        Integer customerId = 31;
        Integer reviewId = 6;

        ReviewVote vote = new ReviewVote();
        vote.setCustomer(new Customer(customerId));
        vote.setReview(new Review(reviewId));
        vote.voteUp();

        ReviewVote saved = repo.save(vote);
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }
}
