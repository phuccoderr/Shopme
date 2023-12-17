package com.shopme.address;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address,Integer> {

    public List<Address> findByCustomer(Customer customer);

    @Query("UPDATE Address a SET a.defaultAddress = true WHERE a.id = ?1")
    @Modifying
    public void setDefaultAdress(Integer id);

    @Query("UPDATE Address a SET a.defaultAddress = false WHERE a.id != ?1 AND a.customer.id = ?2")
    @Modifying
    public void nonDefaultAdress(Integer id,Integer customerId);

    @Query("SELECT a FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
    public Address findByCustomer(Integer addressId,Integer customerId);

    @Query("SELECT a FROM Address  a WHERE a.defaultAddress = true AND a.customer.id = ?1")
    public Address findByDefaultByCustomer(Integer customerId);
}
