package com.shopme.admin.product;
import static org.assertj.core.api.Assertions.assertThat;
import com.shopme.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

    @Autowired private ProductRepository repo;

    @Test
    public void testCreateProduct() {
        Product product = new Product();
        product.setName("Apple MacBook Air 15 M2 (16GB/256GB SSD) (Midnight)");
        product.setAlias("apple macBook air 15 M2 (16GB/256GB SSD) (Midnight)".replace(" ","-"));
        product.setShortDescription("- CPU: Apple M2\n" +
                "- Màn hình: 15.3\" (2880 x 1864) Liquid Retina\n" +
                "- RAM: 16GB / 256GB SSD\n" +
                "- Hệ điều hành: macOS\n" +
                "- Pin: 53 Wh");
        product.setFullDescription("Thiết kế tinh tế và sang trọng, màn hình chống chói tối ưu nhờ tấm nền IPS \n" +
                "Thu hút người nhìn nhờ thiết kế tinh tế, kết hợp cùng tone màu vàng, laptop MacBook Air 2020 13.3\" MGND3SA/A còn nổi bật nhờ logo Apple mạ kim loại sáng bóng được đặt giữa máy. ");
        product.setCreatedTime(new Date());
        product.setEnabled(true);
        product.setPrice(18790.000f);
        product.setSale(10);
        product.setMainImage("");
        Product saved = repo.save(product);

        assertThat(saved.getId()).isGreaterThan(0);

    }
}
