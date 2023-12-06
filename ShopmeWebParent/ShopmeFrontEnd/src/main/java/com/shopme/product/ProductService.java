package com.shopme.product;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    public static final int PRODUCTS_PER_PAGE = 12;
    @Autowired private ProductRepository repo;

    public Page<Product> listByCategory(Integer pageNum, Integer categoryId, Brand brand,String keyword) {
        String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
        Pageable pageable = PageRequest.of(pageNum - 1,PRODUCTS_PER_PAGE);
        if (keyword != null) {
            return repo.search(keyword,pageable);
        }
        if (brand != null) {
            return repo.listByCategoryAndBrand(categoryId,categoryIdMatch,brand.getId(),pageable);
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
