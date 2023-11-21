package com.shopme.admin.brand;
import static org.assertj.core.api.Assertions.assertThat;
import com.shopme.common.entity.Brand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {

    @Autowired private BrandRepository repo;

    @Test
    public void testCreateBrand() {
        Brand brand = new Brand("apple","");
        Brand saved = repo.save(brand);

        assertThat(saved).isNotNull();
    }

    @Test
    public void testGetBrand() {
        Brand brand = repo.findById(1).get();
        brand.setLogo("apple.png");
        assertThat(brand).isNotNull();
    }

    @Test
    public void testGetBrandByName() {
        Brand brand = repo.findByName("apple");
        assertThat(brand).isNotNull();
    }
}
