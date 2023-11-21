package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category,Integer>,PagingAndSortingRepository<Category,Integer>  {
    @Query(value = "SELECT c FROM Category c WHERE c.parent.id is Null")
    public Page<Category> findRootCategories(Pageable pageable);

    @Query(value ="SELECT c FROM Category c WHERE c.parent.id is NULL")
    public List<Category> findRootCategories(Sort sort);

    @Query(value ="SELECT c FROM Category c WHERE c.name LIKE %?1%")
    public Page<Category> search(String keyword, Pageable pageable);

    public Category findByName(String name);
    public Category findByAlias(String alias);

    @Query(value = "UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id ,boolean enabled);
}
