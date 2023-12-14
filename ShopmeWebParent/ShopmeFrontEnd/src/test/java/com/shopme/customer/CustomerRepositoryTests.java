package com.shopme.customer;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CustomerRepositoryTests {
    @Autowired private CustomerRepository repo;

    @Test
    public void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setEmail("phuc@gmail.com");
        customer.setPassword("0123456789");
        customer.setFirstName("Nguyen");
        customer.setLastName("Hoang Phuc");
        customer.setPhoneNumber("0186748784");
        customer.setAddressLine1("Hau Giang");
        customer.setAddressLine2("Can Tho");
        customer.setCity("Can Tho");
        customer.setState("Hung loi");
        customer.setPostalCode("90000");
        customer.setCreatedTime(new Date());
        customer.setEnabled(true);
        customer.setAuthenticationType(AuthenticationType.DATABASE);

        Customer saved = repo.save(customer);
        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void testGetCustomer() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Customer customer = repo.findById(1).get();
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
    }

    @Test
    public void testGetCustomerByCode() {
        Customer customer = repo.findByEmail("phuccoderr");

        System.out.println(customer.getFirstName());
    }
}
