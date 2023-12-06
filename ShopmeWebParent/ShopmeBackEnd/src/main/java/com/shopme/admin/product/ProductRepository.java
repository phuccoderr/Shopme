package com.shopme.admin.product;

import com.shopme.common.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends CrudRepository<Product,Integer>, PagingAndSortingRepository<Product,Integer> {
    public Product findByName(String name);
    @Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
    @Modifying
    public void enabled(Integer id,boolean enabled);

    @Query("SELECT p FROM Product p WHERE p.name like %?1% or " +
            "p.category.name LIKE %?1% or " +
            "p.brand.name LIKE %?1%")
    public Page<Product> findAll(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 or " +
            "p.category.allParentIds like %?2% ")
    public Page<Product> findAllCategory(Integer id, String categoryIdMatch,Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE (p.category.id = ?1"
            + " or p.category.allParentIds like %?2%) AND"
            + " (p.name like %?3%"
            + " or p.category.name like %?3%"
            + " or p.brand.name like %?3%)")
    public Page<Product> searchInCategory(Integer id,String categoryIdMatch,String keyword,Pageable pageable) ;

}
