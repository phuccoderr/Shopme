package com.shopme.order;

import com.shopme.checkout.CheckOutInfo;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.*;
import com.shopme.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {
    public static final int ORDER_PER_PAGE = 5;
    @Autowired private OrderRepository repo;

    public Order createOrder(Customer customer, Address address, List<CartItem> cartItems,
                             PaymentMethod paymentMethod, CheckOutInfo checkOutInfo) {
        Order newOrder = new Order();
        newOrder.setCustomer(customer);
        newOrder.setPaymentMethod(paymentMethod);
        newOrder.setOrderTime(new Date());
        newOrder.setSubtotal(checkOutInfo.getProductTotal());
        newOrder.setShippingCost(20000);
        newOrder.setTotal(checkOutInfo.getPaymentTotal());
        newOrder.setDeliverDays(checkOutInfo.getDeliverDays());
        newOrder.setDeliverDate(checkOutInfo.getDeliverDate());
        newOrder.setStatus(OrderStatus.NEW);

        if (address == null) {
            newOrder.copyAddressFromCustomer();
        } else {
            newOrder.copyShippingAddress(address);
        }

        Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
        for (CartItem item : cartItems) {
            Product product = item.getProduct();

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setSubtotal(product.getPrice());
            orderDetail.setShippingCost(20000);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setUnitPrice(0);

            orderDetails.add(orderDetail);
        }

        List<OrderTrack> orderTracks = newOrder.getOrderTrack();
        OrderTrack newTrack = new OrderTrack();
        newTrack.setOrder(newOrder);
        newTrack.setOrderStatus(OrderStatus.NEW);
        newTrack.setUpdatedTime(new Date());
        newTrack.setNotes(OrderStatus.NEW.defaultDescription());

        orderTracks.add(newTrack);


        return repo.save(newOrder);
    }

    public Page<Order> listByPage(int pageNum,Integer customerId) {
        Sort sort = Sort.by("orderTime");
        sort = sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1,ORDER_PER_PAGE,sort);

        return repo.findAll(customerId,pageable);
    }

    public Order getOrder(Integer id,Customer customer) {
        return repo.findByIdAndCustomer(id,customer);
    }

    public void setOrderReturnRequest(OrderReturnRequest request,Customer customer) {
        Order order = repo.findByIdAndCustomer(request.getOrderId(),customer);

        if (order.isReturnRequest()) return;

        OrderTrack track = new OrderTrack();
        track.setOrder(order);
        track.setUpdatedTime(new Date());
        track.setOrderStatus(OrderStatus.RETURN_REQUEST);

        String notes = "Reason: " + request.getReason();
        if (!"".equals(request.getNote())) {
            notes += ". " + request.getNote();
        }
        track.setNotes(notes);

        order.getOrderTrack().add(track);
        order.setStatus(OrderStatus.RETURN_REQUEST);

        repo.save(order);

    }

}
