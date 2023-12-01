package com.shopme.category;

import com.shopme.common.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category,Integer> {
    @Query("SELECT c FROM Category c where c.enabled = true ORDER BY c.name ASC")
    public List<Category> findAllEnabled();

    @Query("SELECT c FROM Category c where c.enabled = true AND c.parent.id is null ORDER BY c.name ASC")
    public List<Category> findAllEnabledNoParen();

    @Query("SELECT c FROM Category c where c.enabled = true AND c.alias = ?1")
    public Category findByAliasEnabled(String alias);

}
