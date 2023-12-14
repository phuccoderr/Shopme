package com.shopme.address;
import static org.assertj.core.api.Assertions.assertThat;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class AddressRepositoryTests {

    @Autowired
    private AddressRepository repo;

    @Test
    public void testAddNew() {
        Address newAddress = new Address();

        newAddress.setCountry(new Country(243));
        newAddress.setCustomer(new Customer(30));
        newAddress.setFirstName("Phi");
        newAddress.setPhoneNumber("+84978308851");
        newAddress.setAddressLine1("Quan 9");
        newAddress.setAddressLine2("Quan 10");
        newAddress.setCity("HCM");
        newAddress.setState("HCM");
        newAddress.setPostalCode("123466");

        Address saved = repo.save(newAddress);

        assertThat(saved).isNotNull();
    }



}
