package com.shopme.admin.customer;

import com.shopme.admin.brand.BrandNotFoundException;
import com.shopme.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CustomerService {
    public static final int CUSTOMER_PER_PAGE = 10;
    @Autowired private CustomerRepository repo;

    public Page<Customer> listByPage(int pageNum,String sortField,String sortDir,String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1,CUSTOMER_PER_PAGE,sort);

        if (keyword != null) {
            return repo.findAll(keyword,pageable);
        }
        return repo.findAll(pageable);
    }

    public Customer get(Integer id) throws CustomerNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new CustomerNotFoundException("Cloud any cusomter with id: " + id);
        }
    }

    public void save(Customer customer) {
        Customer customerInDB = repo.findById(customer.getId()).get();
        if (customerInDB != null) {
            customer.setCreatedTime(customerInDB.getCreatedTime());
        }
        repo.save(customer);

    }

    public void delete(Integer id) throws CustomerNotFoundException {
        Long findById = repo.countById(id);
        if (findById == 0 || findById == null) {
            throw new CustomerNotFoundException("Cloud not find any customer  with ID:" + id);
        }
        repo.deleteById(id);
    }
}
