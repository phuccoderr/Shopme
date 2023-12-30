package com.shopme.admin.review;

import com.shopme.common.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
    @Query("SELECT r FROM Review r WHERE r.customer.firstName like %?1% or r.customer.lastName like %?1%")
    public Page<Review> findAll(String keyword, Pageable pageable);

    public Long countById(Integer id);
}
