package com.shopme.admin.review;

import com.shopme.admin.brand.BrandNotFoundException;
import com.shopme.admin.product.ProductNotFoundException;
import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewService {
    public static final int REVIEW_PER_PAGE = 10;
    @Autowired private ReviewRepository repo;
    @Autowired private ProductRepository productRepo;

    public List<Review> listByPage() {
        return repo.findAll();
    }

    public Review get(Integer id) throws ReviewNotFoundException {
        try {
            return repo.findById(id).get();
        } catch(NoSuchElementException e) {
            throw new ReviewNotFoundException("Could not find any review with ID " + id);
        }
    }

    public Page<Review> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1,REVIEW_PER_PAGE,sort);

        if (keyword != null) {
            return repo.findAll(keyword,pageable);
        }
        return repo.findAll(pageable);
    }


    public void delete(Integer id) throws ReviewNotFoundException {
        Long countById = repo.countById(id);
        if (countById == null || countById ==0) {
            throw new ReviewNotFoundException("Cloud not find any review  with ID:" + id);
        }
        repo.deleteById(id);
    }
}
