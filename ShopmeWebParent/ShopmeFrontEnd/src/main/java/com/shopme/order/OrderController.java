package com.shopme.order;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.customer.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class OrderController {
    @Autowired private OrderService service;
    @Autowired private CustomerService customerService;

    @GetMapping("/orders")
    public String listFirstPage(Model model,HttpServletRequest request) {
        return listByPage(1,model,request);
    }

    @GetMapping("/orders/page/{pageNum}")
    private String listByPage(@PathVariable("pageNum") Integer pageNum, Model model, HttpServletRequest request) {
        Customer customer = getAuthenticationCustomer(request);
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

    private Customer getAuthenticationCustomer(HttpServletRequest request) {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetail(Model model,
                                  @PathVariable(name = "id")Integer id,HttpServletRequest request) {
        Customer customer = getAuthenticationCustomer(request);

        Order order = service.getOrder(id,customer);
        model.addAttribute("order",order);

        return "order/order_detail_modal";
    }


}
