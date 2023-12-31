package com.shopme.review;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.product.Product;
import com.shopme.order.OrderDetailRepository;
import com.shopme.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ReviewService {
    public static final int REVIEW_PER_PAGE = 10;
    @Autowired private ReviewRepository repo;
    @Autowired private ProductRepository productRepo;
    @Autowired private OrderDetailRepository orderDetailRepo;

    public Page<Review> listByCustomerByPage(int pageNum,
                                             String sortField,
                                             String sortDir,
                                             String keyword,
                                             Integer customerId) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1,REVIEW_PER_PAGE,sort);

        if (keyword != null && !keyword.isEmpty()) {
            return repo.findByCustomer(customerId,keyword,pageable);
        }

        return repo.findByCustomer(customerId,pageable);
    }

    public Review getReviewByCustomer(Integer customerId,Integer reviewId) {
        return repo.findByCustomerAndId(customerId, reviewId);
    }

    public Page<Review> listByProduct(Product product,
                                      int pageNum,
                                      String sortField,
                                      String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1,REVIEW_PER_PAGE,sort);
        return repo.findByProduct(product.getId(),pageable);
    }

    public boolean didCustomerReviewProduct(Customer customer,Integer productId) {
        Long count = repo.countByCustomerAndProduct(customer.getId(), productId);
        return count > 0;
    }
    public boolean canCustomerReviewProduct(Customer customer,Integer productId) {
        Long count = orderDetailRepo.countByProductAndCustomerAndOrderStatus(productId,customer.getId(), OrderStatus.DELIVERED);
        return count > 0;
    }

    public Review save(Review review) {
        review.setReviewTime(new Date());
        Review saved = repo.save(review);
        Integer productId = saved.getProduct().getId();
        productRepo.updateReviewCountAndAverageRating(productId);
        return saved;
    }
}
