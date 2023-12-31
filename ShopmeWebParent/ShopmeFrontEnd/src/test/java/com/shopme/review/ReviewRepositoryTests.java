package com.shopme.review;

import com.shopme.common.entity.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ReviewRepositoryTests {
    @Autowired private ReviewRepository repo;

    @Test
    public void testUpdateVoteCount() {
        Integer reviewId = 6;
        repo.updateVoteCount(reviewId);
        Review review = repo.findById(reviewId).get();

        Assertions.assertThat(review.getVotes()).isEqualTo(2);
    }
}
