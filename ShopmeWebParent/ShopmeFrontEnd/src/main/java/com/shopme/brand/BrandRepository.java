package com.shopme.brand;

import com.shopme.common.entity.Brand;
import org.springframework.data.repository.CrudRepository;

public interface BrandRepository extends CrudRepository<Brand,Integer> {

    public Brand findByName(String name);
}
