package com.shopme.address;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressService {
    @Autowired private AddressRepository repo;

    public List<Address> listByCustomer(Customer customer) {
        return repo.findByCustomer(customer);
    }

    public void updateDefaultAddressByCustomer(Integer addressId,Integer customerId) {
        if (addressId > 0) {
            repo.setDefaultAdress(addressId);
        }
        repo.nonDefaultAdress(addressId,customerId);
    }



    public void save(Address address, Customer customer) {
        address.setDefaultAddress(false);
        address.setCustomer(customer);
        repo.save(address);

    }



    public Address get(Integer addressId, Integer customerId) {
        return repo.findByCustomer(addressId,customerId);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }

    public Address getDefaultAddress(Customer customer) {
        return repo.findByDefaultByCustomer(customer.getId());
    }
}
