package com.shopme.category;
import static org.assertj.core.api.Assertions.assertThat;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTests {
    @Autowired private CategoryRepository repo;

    @Test
    public void testListByEnabled() {
        List<Category> categories = repo.findAllEnabled();
        for (Category category : categories) {
            System.out.println(category.getName());
        }
        assertThat(categories.size()).isGreaterThan(0);
    }


    @Test
    public void testGetCategory() {
        List<Category> categoriesAll  = (List<Category>) repo.findAll();

        List<Category> categories = new ArrayList<>();
        List<Category> categories2 = new ArrayList<>();
        List<Category> categories3 = new ArrayList<>();

        for (Category cate : categoriesAll) {
            Set<Category> children = cate.getChildren();
            //add 1
            if (cate.getParent() == null) {
                categories.add(cate);
            }

            //add2
            for (Category child : children) {
                categories.add(child);
                Set<Category> childLast = child.getChildren();


                //add 3
                for (Category chil : childLast) {
                    categories.add(chil);
                }
            }

        }

        //out
        for (Category cate : categories ) {
            System.out.println("Category 1:" + cate.getName());
        }
        for (Category cate : categories2 ) {
            System.out.println("Category 2:" + cate.getName());
        }

    }
}
