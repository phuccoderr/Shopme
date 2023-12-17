package com.shopme.product;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
        Set<Product> resultSet = new HashSet<>();
        Pageable pageable = PageRequest.of(pageNum - 1,PRODUCTS_PER_PAGE);


        if (keyword != null && !keyword.isEmpty()) {
            return repo.search(keyword,pageable);
        }

        if (sortField != null && !sortField.isEmpty()) {
            Sort sort;
            if (sortField.equals("lowPrice")) {
                sort = Sort.by("price");
                sort = sort.ascending();
            } else if (sortField.equals("highPrice")) {
                sort = Sort.by("price");
                sort = sort.descending();
            } else if (sortField.equals("createdTime")) {
                sort = Sort.by("createdTime");
                sort = sort.descending();
            } else {
                sort = Sort.by("name");
                sort = sort.ascending();
            }
            pageable = PageRequest.of(pageNum - 1,PRODUCTS_PER_PAGE,sort);
            return repo.findByPrice(pageable);
        }





        if (brand != null ) {
            return repo.listByBrand(brand.getId(),pageable);
        }
        return repo.listByCategory(categoryId,categoryIdMatch,pageable);
    }

    public Product getProduct(String alias) {
        return repo.findByAlias(alias);
    }

    public Page<Product> search(String keyword,Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum,PRODUCTS_PER_PAGE);
        return repo.search(keyword,pageable);
    }

}
