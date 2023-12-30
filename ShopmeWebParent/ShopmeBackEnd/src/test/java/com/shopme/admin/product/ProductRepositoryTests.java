package com.shopme.admin.product;
import static org.assertj.core.api.Assertions.assertThat;
import com.shopme.common.entity.product.Product;
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
        product.setFullDescription("Các dòng sản phẩm của thương hiệu Apple luôn được đánh giá cao về chất lượng cũng như về hình dáng, mẫu mã. Nếu bạn đang cần một chiếc laptop mỏng, nhẹ, nhỏ gọn để tiện mang theo bên mình thì lựa chọn Laptop APPLE MacBook Air 2020 MGN63SA/A là vô cùng hợp lý.\n" +
                "Vỏ nhôm xám thời thượng. Thiết kế tối giản, tinh tế \n" +
                "Laptop APPLE MacBook Air 2020 MGN63SA/A có vỏ máy được nhà sản xuất giới thiệu làm từ nhôm tái chế 100% góp phần bảo vệ môi trường và mang tới độ bền cao cho máy tính xách tay này. Bởi đây là nguyên liệu chịu được va đập khá tốt, có khả năng bảo vệ được các linh kiện của máy trong trường hợp xảy ra va đập mạnh. MacBook Air 2020 MGN63SA/A có kích tước là 30.41 x 21.24 x 0.41–1.61 cm mang tới ưu điểm về ngoại hình gọn nhẹ cho chiếc máy tính này so với các sản phẩm khác cùng phân khúc. Trọng lượng máy chỉ khoảng 1.3kg, cho phép người dùng thoải mái mang theo bên mình cả một ngày dài mà không hề thấy bất tiện.\n" +
                "\n" +
                "Màn hình rộng 13.3 inch có độ phân giải cao cho chất lượng hình ảnh sống động\n" +
                "Máy tính xách tay APPLE MacBook Air 2020 MGN63SA/A được trang bị màn hình rộng 13.3 inch với độ phân giải lên tới 2560 x 1600 pixels cho chất lượng hình ảnh hiển thị sắc nét, màu sắc sống động và chân thật. Ngoài ra, màn hình của máy tính này còn sử dụng tấm nền IPS không cảm ứng. Nhờ vậy màn hình này có góc nhìn tương đối rộng, người xem có thể thoải mái nhìn ở bất cứ góc độ nào cũng có thể thấy được hình ảnh hiển thị trên đó.\n" +
                "\n" +
                "Cấu hình có cải tiến so với thế hệ trước với bộ vi xử lý Apple M1, RAM 8GB, ổ cứng 256GB SSD\n" +
                "APPLE MacBook Air 2020 MGN63SA/A được tích hợp bộ vi xử lý Apple M1 với cải tiến về công nghệ sản xuất, CPU này mang tới hiệu năng xử lý tốc độ cao hơn tới 3,5 lần so với thế hệ CPU Apple trước đó. Nó có thể xử lý được các tác vụ đồ họa 3D mà chỉ tiêu thụ một mức năng lượng rất khiêm tốn.Sử dụng bộ nhớ trong 8GB Onboard LPDDR4 3733MHz và ổ cứng có dung lượng 256GB SSD không chỉ mang tới không gian lưu trữ tương đối rộng rãi cho người dùng mà còn làm tăng thêm sức mạnh xử lý tác vụ cho chiếc máy tính này. Nó có thể xử lý trơn tru, nhanh chóng các tác vụ đa nhiệm, tốc độ xử lý các thao tác trên máy tính cực nhanh như đóng mở các tab, khởi động máy,....\n" +
                "\n" +
                "Bàn phím Magic Keyboard mang tới trải nghiệm gõ phím ấn tượng\n" +
                "Laptop APPLE MacBook Air 2020 MGN63SA/A sử dụng bàn phím được thiết kế riêng biệt cho các dòng sản phẩm của Apple: Magic Keyboard. Hình trình phím chỉ 1mm, độ nảy tốt, êm ái mang tới những trải nghiệm gõ phím ấn tượng cho người sử dụng chiếc laptop này");
        product.setCreatedTime(new Date());
        product.setEnabled(true);
        product.setPrice(18790.000f);
        product.setSale(10);
        product.setMainImage("");
        Product saved = repo.save(product);

        assertThat(saved.getId()).isGreaterThan(0);

    }

    @Test
    public void testSetProductRela() {
        Product product = repo.findById(1).get();
        product.addDetail("HDD","500gb");
        product.addExtraImage("nothing.png");
        repo.save(product);
        System.out.println(product.getProductImages());
        assertThat(product).isNotNull();
    }

    @Test
    public void testGetByName() {
        Product product = repo.findByName("Apple MacBook Air 15 M2 (16GB/256GB SSD) (Midnight)");

        System.out.println(product.getName());
        assertThat(product).isNotNull();
    }

    @Test
    public void testCreateRelaImage() {

        Product product = repo.findById(1).get();

        repo.save(product);


        assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateReviewCountAndAverageRating() {
        Integer productId = 2;
        repo.updateReviewCountAndAverageRating(productId);
    }
}
