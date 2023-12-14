package com.shopme.admin.shippingrate;
import static org.assertj.core.api.Assertions.assertThat;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ShippingRatesRepositoryTests {
    @Autowired private ShippingRateRepository repo;

    @Test
    public void createShippingRate() {
        Country country = new Country(243);
        ShippingRate sr = new ShippingRate();
        sr.setState("Can Tho");
        sr.setDays(6);
        sr.setRate(49);
        sr.setCountry(country);
        sr.setCodSupported(true);

        ShippingRate saved = repo.save(sr);

        assertThat(saved).isNotNull();
    }


}
