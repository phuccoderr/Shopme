package com.shopme.admin.product;

import com.shopme.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired private ProductRepository repo;

    public List<Product> listAll () {
        return (List<Product>) repo.findAll();
    }

    public Product get(Integer id) {
        return repo.findById(id).get();
    }

    public String checkUnique(Integer id, String name) {
        boolean isProductNew = (id == null || id == 0);
        Product product = repo.findByName(name);
        if (isProductNew) {
            if(product != null) {
                return "Duplicated";
            }
        } else {
            if(product != null || product.getId() != id) {
                return "Duplicated";
            }
        }
        return "OK";
    }
}
