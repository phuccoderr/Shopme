package com.shopme.common.entity.order;

import com.shopme.common.entity.AbstractAddress;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends AbstractAddress {
    @Column(length = 45,nullable = false)
    private String country;

    private Date orderTime;

    private float ShippingCost;
    private float subtotal;
    private float total;
    private float productCost;

    private int deliverDays;

    private Date deliverDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("updatedTime ASC")
    private List<OrderTrack> orderTrack = new ArrayList<>();

    public Order(Integer id,Date orderTime,float productCost, float subtotal, float total) {

        this.id = id;
        this.orderTime = orderTime;
        this.productCost = productCost;
        this.subtotal = subtotal;
        this.total = total;
    }

    public void copyAddressFromCustomer() {
        setFirstName(customer.getFirstName());
        setLastName(customer.getLastName());
        setAddressLine1(customer.getAddressLine1());
        setAddressLine2(customer.getAddressLine2());
        setPhoneNumber(customer.getPhoneNumber());
        setState(customer.getState());
        setCity(customer.getCity());
        setCountry(customer.getCountry().getName());
        setPostalCode(customer.getPostalCode());
    }

    public void copyShippingAddress(Address address) {
        setFirstName(address.getFirstName());
        setLastName(address.getLastName());
        setAddressLine1(address.getAddressLine1());
        setAddressLine2(address.getAddressLine2());
        setPhoneNumber(address.getPhoneNumber());
        setState(address.getState());
        setCity(address.getCity());
        setCountry(address.getCountry().getName());
        setPostalCode(address.getPostalCode());
    }

    @Transient
    public String getDestination() {
        String address = country;

        if (!addressLine1.isEmpty()) address += ", " + addressLine1;

        if (addressLine2 != null && !addressLine2.isEmpty() ) address += ", " + addressLine1;

        if (!city.isEmpty()) address += ", " + city;

        if (state != null && !state.isEmpty()) address += " " + state;

        if (country != null && !country.isEmpty()) address += " " + country;

        return address;
    }

    @Transient
    public String getProductNames() {
        String productNames = "";

        productNames += "<ul>";
        for (OrderDetail detail : orderDetails) {
            productNames += "<li class=\"list-group-item d-flex justify-content-between align-items-center\">"
                    + "<div class=\"mt-1 me-auto\">\n"
                    + "<div>"+ detail.getProduct().getName() + "</div>\n"
                    + "</div>"
                    + "<span class=\"badge bg-secondary rounded-pill\">" + detail.getQuantity() + "</span>"
                    + "</li>";
        }
        productNames += "</ul>";

        return productNames;
    }

    @Transient
    public String getDeliverDateOnForm() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(this.deliverDate);
    }
    public void setDeliverDateOnForm(String date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.deliverDate = dateFormat.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean hasStatus(OrderStatus orderStatus) {
        for (OrderTrack track : orderTrack) {
            if (track.getOrderStatus().equals(orderStatus)) {
                return true;
            }
        }
        return false;
    }
    @Transient
    public boolean isReturnRequest() {
        return hasStatus(OrderStatus.RETURN_REQUEST);
    }

    @Transient
    public boolean isReturned() {
        return hasStatus(OrderStatus.RETURNED);
    }

    @Transient
    public boolean isCOD() {
        return paymentMethod.equals(PaymentMethod.COD);
    }

    @Transient
    public boolean isProcessing() {
        return hasStatus(OrderStatus.PROCESSING);
    }
    @Transient
    public boolean isPicked() {
        return hasStatus(OrderStatus.PICKED);
    }
    @Transient
    public boolean isShipping() {
        return hasStatus(OrderStatus.SHIPPING);
    }
    @Transient
    public boolean isDelivered() {
        return hasStatus(OrderStatus.DELIVERED);
    }

}
