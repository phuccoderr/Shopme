package com.shopme;

import com.shopme.category.CategoryRepository;
import static org.assertj.core.api.Assertions.assertThat;
import com.shopme.common.entity.Category;
import com.shopme.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {
    @Autowired private ProductRepository repo;
    @Autowired private CategoryRepository cateRepo;

    @Test
    public void testGetListProduct() {

    }
}
