package com.shopme.admin.customer;

import com.shopme.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends CrudRepository<Customer,Integer>, PagingAndSortingRepository<Customer,Integer> {
    public Customer findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.firstName like %?1% or c.lastName like %?1%")
    public Page<Customer> findAll(String keyword, Pageable pageable);

    public Long countById(Integer id);
}
