package com.shopme.product;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {
    public static final int PRODUCTS_PER_PAGE = 12;
    public static final long SREACH_RESULTS_PER_PAGE = 12;
    @Autowired private ProductRepository repo;

    public Page<Product> listByCategory(Integer pageNum, Integer categoryId, Brand brand,
                                        String sortField,
                                        String keyword) {
        String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
        Pageable pageable = PageRequest.of(pageNum - 1,PRODUCTS_PER_PAGE);
        Specification<Product> spec = Specification.where(null);

        spec = spec.and(ProductSpecifications.withCategory(categoryId,categoryIdMatch));
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(ProductSpecifications.withSearch(keyword));
        }
        if (brand != null ) {
            spec = spec.and(ProductSpecifications.withBrand(brand.getId()));
        }
        if (sortField != null && !sortField.isEmpty()) {
            Sort sort;
            if (sortField.equals("lowPrice")) {
                sort = Sort.by("price");
                sort = sort.ascending();
                return repo.findAll(spec, PageRequest.of(pageNum - 1,PRODUCTS_PER_PAGE,sort));
            } else if (sortField.equals("highPrice")) {
                sort = Sort.by("price");
                sort = sort.descending();
                return repo.findAll(spec, PageRequest.of(pageNum - 1,PRODUCTS_PER_PAGE,sort));
            } else if (sortField.equals("createdTime")) {
                sort = Sort.by("createdTime");
                sort = sort.descending();
                return repo.findAll(spec, PageRequest.of(pageNum - 1,PRODUCTS_PER_PAGE,sort));
            } else {
                sort = Sort.by("name");
                sort = sort.ascending();
                return repo.findAll(spec, PageRequest.of(pageNum - 1,PRODUCTS_PER_PAGE,sort));
            }
        }
        return repo.findAll(spec, pageable);
    }

    public Product getProduct(String alias) throws ProductNotFoundException {
        Product product = repo.findByAlias(alias);
        if (product == null) {
            throw new ProductNotFoundException("Could not find any product with alias " + alias);
        }
        return product;
    }


    public Product getProduct(Integer id) throws ProductNotFoundException {
        Product product = repo.findById(id).get();
        if (product == null) {
            throw new ProductNotFoundException("Could not find any product with Id " + id);
        }
        return product;
    }

}
