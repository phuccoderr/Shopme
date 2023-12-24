package com.shopme.admin.order;

import com.shopme.common.entity.order.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class OrderRepositoryTests {
    @Autowired private OrderRepository repo;

    @Test
    public void testFindByOrderTimeBetween() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = dateFormat.parse("2023-12-01");
        Date endTime = dateFormat.parse("2023-12-31");

        List<Order> listOrders = repo.findByOrderTimeBetWeen(startTime,endTime);

        for (Order order : listOrders) {
            System.out.printf("%s | %s | %.2f | %.2f \n",order.getId(),order.getOrderTime(),
                    order.getSubtotal(),order.getTotal());
        }
    }
}
