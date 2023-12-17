package com.shopme.order;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order,Integer>, PagingAndSortingRepository<Order,Integer> {
    @Query("SELECT o FROM Order o WHERE o.customer.id = ?1")
    public Page<Order> findAll(Integer customerId, Pageable pageable);

    public Order findByIdAndCustomer(Integer id, Customer customer);


}
