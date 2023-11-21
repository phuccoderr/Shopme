package com.shopme.admin.product;

import com.shopme.common.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product,Integer> {

}
