package com.shopme.order;

import com.shopme.common.entity.order.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class OrderDetailRepositoryTests {
    @Autowired private OrderDetailRepository repo;

    @Test
    public void testCountByProductAndCustomerAndOrderStatus() {
         Integer productId = 2;
         Integer customerId = 30;

         Long count = repo.countByProductAndCustomerAndOrderStatus(productId,customerId, OrderStatus.DELIVERED);
         Assertions.assertThat(count).isGreaterThan(0);
    }
}
