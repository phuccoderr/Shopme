package com.shopme.product;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product,Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled = true AND (p.category.id = ?1 or p.category.allParentIds like %?2%)")
    public List<Product> listByCategory(Integer cateId,String categoryIdMatch);

}
