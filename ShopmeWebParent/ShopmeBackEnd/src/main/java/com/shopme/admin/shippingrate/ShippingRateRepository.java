package com.shopme.admin.shippingrate;

import com.shopme.common.entity.ShippingRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;



public interface ShippingRateRepository extends CrudRepository<ShippingRate,Integer>,
        PagingAndSortingRepository<ShippingRate,Integer> {

    @Query("SELECT s FROM ShippingRate s WHERE s.country.name LIKE %?1% OR s.state LIKE %?1%")
    public Page<ShippingRate> findAll(String keyword, Pageable page);

    @Query("SELECT s from ShippingRate s WHERE s.country.id = ?1 AND s.state = ?2")
    public ShippingRate findByCountryAndState(Integer countryId, String state);
    @Query("UPDATE ShippingRate s SET s.codSupported = ?2 WHERE s.id = ?1")
    @Modifying
    public void enabled(Integer id, boolean status);

    public Long countById(Integer id);
}
