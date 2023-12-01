package com.shopme.product;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired private ProductRepository repo;

    public List<Product> listByCategory(Integer categoryId) {
        String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
        return repo.listByCategory(categoryId,categoryIdMatch);
    }
}
