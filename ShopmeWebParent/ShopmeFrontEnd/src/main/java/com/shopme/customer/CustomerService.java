package com.shopme.customer;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.country.CountryRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CustomerService {
    @Autowired private CustomerRepository repo;
    @Autowired private CountryRepository countryRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    public List<Country> listCountries() {
        return countryRepo.findAllByOrderByNameAsc();
    }

    public void register(Customer customer) {
        passwordEncoder(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.DATABASE);
        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);

        repo.save(customer);
    }

    public void passwordEncoder(Customer customer) {
        String encoder = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encoder);
    }

    public boolean checkUnique(String email) {
        Customer customer = repo.findByEmail(email);
        return customer == null;
    }

    public Customer getCustomerByEmail(String email) {
        return repo.findByEmail(email);
    }





    public boolean verify(String code) {
        Customer customer = repo.findByVerificationCode(code);
        if (customer == null || customer.isEnabled()) {
            return false;
        } else {
            repo.verifyCustomer(customer.getId());
            return true;
        }
    }

    public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode, AuthenticationType authenticationType) {
        Customer customer = new Customer();
        customer.setFirstName(name);
        setName(customer,name);

        customer.setEmail(email);
        customer.setPassword("");
        customer.setCreatedTime(new Date());
        customer.setEnabled(true);
        customer.setAddressLine1("");
        customer.setAddressLine2("");
        customer.setPhoneNumber("");
        customer.setPostalCode("");
        customer.setCountry(countryRepo.findByCode(countryCode));
        customer.setAuthenticationType(authenticationType);

        repo.save(customer);
    }

    private void setName(Customer customer, String name) {
        String[] nameArray = name.split(" ");
        if (nameArray.length < 2) {
            customer.setFirstName(name);
            customer.setLastName("");
        } else {
            customer.setFirstName(nameArray[0]);

            String lastName = nameArray[1] + nameArray[2];
            customer.setLastName(lastName);
        }
    }

    public void updateAuthentication(Customer customer, AuthenticationType authenticationType) {
        if (!customer.getAuthenticationType().equals(authenticationType)) {
            repo.updateAuthenticationType(customer.getId(),authenticationType);
        }
    }

    public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
        Customer customer = repo.findByEmail(email);
        if (customer != null) {
            String token = RandomString.make(30);
            customer.setResetPasswordToken(token);
            repo.save(customer);
            return token;
        } else {
            throw new CustomerNotFoundException("Cloud not find any Customer with email: " + email);
        }

    }

    public Customer getByResetPasswordToken(String token) {
        return repo.findByResetPasswordToken(token);
    }

    public void updatePassword(String token, String password) throws CustomerNotFoundException {
        Customer customer = repo.findByResetPasswordToken(token);
        if (customer == null) {
            throw new CustomerNotFoundException("No Customer found: Invalid Token");
        }

        customer.setPassword(password);
        passwordEncoder(customer);
        customer.setResetPasswordToken(null);
        repo.save(customer);
    }

    public void update(Customer customer) {
        Customer customerInDB = repo.findById(customer.getId()).get();

        if (customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
            if (!customer.getPassword().isEmpty()) {
                String encode = passwordEncoder.encode(customer.getPassword());
                customer.setPassword(encode);
            } else {
                customer.setPassword(customerInDB.getPassword());
            }
        } else {
            customer.setPassword(customerInDB.getPassword());
        }

        customer.setEnabled(true);
        customer.setCreatedTime(customerInDB.getCreatedTime());
        customer.setAuthenticationType(customerInDB.getAuthenticationType());

        repo.save(customer);
    }
}
