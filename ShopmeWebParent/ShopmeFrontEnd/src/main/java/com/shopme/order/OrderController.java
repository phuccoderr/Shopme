package com.shopme.order;

import com.shopme.ControllerHelper;
import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.product.Product;
import com.shopme.customer.CustomerService;
import com.shopme.review.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Iterator;
import java.util.List;

@Controller
public class OrderController {
    @Autowired private OrderService service;
    @Autowired private ReviewService reviewService;
    @Autowired private ControllerHelper controllerHelper;

    @GetMapping("/orders")
    public String listFirstPage(Model model,HttpServletRequest request) {
        return listByPage(1,model,request);
    }

    @GetMapping("/orders/page/{pageNum}")
    private String listByPage(@PathVariable("pageNum") Integer pageNum, Model model, HttpServletRequest request) {
        Customer customer = controllerHelper.getAuthenticationCustomer(request);
        Page<Order> page = service.listByPage(1,customer.getId());
        List<Order> listOrders = page.getContent();

        //pagination
        long startCount = (pageNum - 1) * service.ORDER_PER_PAGE + 1;
        long endCount = startCount + service.ORDER_PER_PAGE - 1;

        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("endCount",endCount);
        model.addAttribute("startCount",startCount);
        model.addAttribute("listOrders",listOrders);

        return "order/order";
    }


    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetail(Model model,
                                  @PathVariable(name = "id")Integer id,HttpServletRequest request) {
        Customer customer = controllerHelper.getAuthenticationCustomer(request);
        Order order = service.getOrder(id,customer);

        setProductReviewStatus(customer,order);

        model.addAttribute("order",order);

        return "order/order_detail_modal";
    }

    private void setProductReviewStatus(Customer customer, Order order) {
        Iterator<OrderDetail> iterator = order.getOrderDetails().iterator();
        while (iterator.hasNext()) {
            OrderDetail detail = iterator.next();
            Product product = detail.getProduct();
            Integer productId = product.getId();

            boolean didCustomerReviewProduct = reviewService.didCustomerReviewProduct(customer, productId);
            product.setReviewedByCustomer(didCustomerReviewProduct);

            if (!didCustomerReviewProduct) {
                boolean canCustomerReviewProduct = reviewService.canCustomerReviewProduct(customer, productId);
                product.setCustomerCanReview(canCustomerReviewProduct);
            }
        }

    }


}
