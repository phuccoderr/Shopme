package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BrandRepository extends CrudRepository<Brand,Integer>, PagingAndSortingRepository<Brand,Integer> {

    public Brand findByName(String name);
    @Query(value = "SELECT b FROM Brand b where b.name like %?1%")
    public Page<Brand> findAll(String keyword, Pageable pageable);

}
