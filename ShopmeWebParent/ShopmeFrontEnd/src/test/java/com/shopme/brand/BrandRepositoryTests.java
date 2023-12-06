package com.shopme.brand;

import com.shopme.common.entity.Brand;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BrandRepositoryTests {
    @Autowired private BrandRepository repo;

    @Test
    public void testGetByName() {
        String name = "APPLe";
        Brand brand = repo.findByName(name);
        System.out.println(brand.getName());
        assertThat(brand).isNotNull();
    }

}
