package com.shopme.product;

import com.shopme.common.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> , JpaSpecificationExecutor<Product> {


    public Product findByAlias(String alias);
    @Query(value = "SELECT * FROM Products "
            + "WHERE enabled = true and MATCH(name, short_description, full_description) AGAINST (?1)"
            ,nativeQuery = true)
    public Page<Product> search(String keyword, Pageable pageable);


    @Query("UPDATE Product p SET p.averageRating = COALESCE(CAST((SELECT AVG(r.rating) FROM Review r WHERE r.product.id = ?1) AS Float), 0)," +
            " p.reviewCount = (SELECT COUNT(r.id) FROM Review r WHERE r.product.id = ?1)" +
            " WHERE p.id = ?1")
    @Modifying
    public void updateReviewCountAndAverageRating(Integer productId);


}
