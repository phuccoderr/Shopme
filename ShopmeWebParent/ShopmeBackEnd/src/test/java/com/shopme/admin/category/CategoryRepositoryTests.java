package com.shopme.admin.category;
import static org.assertj.core.api.Assertions.assertThat;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {
    @Autowired private CategoryRepository repo;

    @Test
    public void testCreateCategory() {
        Category laptop = new Category();

        laptop.setName("Laptop");
        laptop.setAlias("laptop");
        laptop.setImg("laptop.png");

        repo.save(laptop);
    }

    @Test
    public void testCreateParent() {
        Category laptop = new Category(1);
        Category ltth = new Category();
        ltth.setName("Laptop Theo Cau Hinh");
        ltth.setAlias("laptop-theo-cau-hinh");
        ltth.setImg("laptoptheocauhinh.png");
        ltth.setParent(laptop);
        Category saved = repo.save(ltth);

        assertThat(saved).isNotNull();
    }

    @Test
    public void testGetCate() {
        Category category = repo.findByName("Acer");
        System.out.println(category.getName());
        assertThat(category).isNotNull();
    }

    @Test
    public void testDelete() {
        Category category = repo.findById(53).get();

        repo.deleteById(53);
        assertThat(category).isNull();
    }


}
