package com.shopme.admin.review;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ReviewRepositoryTests {
    @Autowired private ReviewRepository repo;

    @Test
    public void testCreate() {
        Review review = new Review();
        review.setCustomer(new Customer(30));
        review.setProduct(new Product(3));
        review.setHeadline("giao sai hàng");
        review.setComment("mua màu tím la giao màu xanh");
        review.setRating(3);
        review.setReviewTime(new Date());
        Review saved = repo.save(review);
        Assertions.assertThat(saved).isNotNull();
    }

}
