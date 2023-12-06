package com.shopme.product;

import com.shopme.common.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends CrudRepository<Product,Integer>, PagingAndSortingRepository<Product,Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled = true AND (p.category.id = ?1 or p.category.allParentIds like %?2%)")
    public Page<Product> listByCategory(Integer cateId, String categoryIdMatch,Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.enabled = true AND (p.category.id = ?1 or p.category.allParentIds like %?2%)" +
            " AND p.brand.id = ?3")
    public Page<Product> listByCategoryAndBrand(Integer cateId,String categoryIdMatch,Integer brandId,Pageable pageable);

    public Product findByAlias(String alias);
    @Query(value = "SELECT * FROM Products "
            + "WHERE enabled = true and MATCH(name, short_description, full_description) AGAINST (?1)"
            ,nativeQuery = true)
    public Page<Product> search(String keyword, Pageable pageable);






}
