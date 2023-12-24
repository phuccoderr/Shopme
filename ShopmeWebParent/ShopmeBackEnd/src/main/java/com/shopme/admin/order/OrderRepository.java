package com.shopme.admin.order;

import com.shopme.common.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order,Integer>, PagingAndSortingRepository<Order,Integer> {
    @Query("SELECT o FROM Order o WHERE o.customer.firstName like %?1% OR o.customer.lastName like %?1%" +
            " OR o.addressLine1 like %?1% OR o.addressLine2 like %?1% OR o.city like %?1%")
    public Page<Order> findAll(String keyword, Pageable pageable);


    @Query("SELECT NEW com.shopme.common.entity.order.Order(o.id,o.orderTime,o.productCost,o.subtotal,o.total)"
            + " FROM Order o WHERE o.orderTime BETWEEN ?1 AND ?2 ORDER BY o.orderTime ASC")
    public List<Order> findByOrderTimeBetWeen(Date startTime,Date endTime);
}
