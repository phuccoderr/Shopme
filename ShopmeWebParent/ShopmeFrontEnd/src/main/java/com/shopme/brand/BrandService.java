package com.shopme.brand;

import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandService {
    @Autowired private BrandRepository repo;

    public Brand getByName(String name) {
        return repo.findByName(name);
    }
}
