package com.shopme.shippingrate;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShippingRateRepositoryTests {
    @Autowired private ShippingRateRepository repo;

    @Test
    public void testGetByCustomer() {
        Country country = new Country(242);
        System.out.println(country.getName());
        ShippingRate shippingRate = repo.findByCountryAndState(country,"Hau Giang");
        System.out.println(shippingRate.getDays());


    }
}
