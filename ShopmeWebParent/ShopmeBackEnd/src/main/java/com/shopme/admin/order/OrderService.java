package com.shopme.admin.order;

import com.shopme.admin.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.User;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {
    public static final int ORDER_PER_PAGE = 10;

    @Autowired private OrderRepository repo;
    @Autowired private CountryRepository countryRepo;

    public Page<Order> listByPage(int pageNum,String sortField,String sortDir,String keyword) {

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1,ORDER_PER_PAGE,sort);
        if (keyword != null) {
            return repo.findAll(keyword,pageable);
        }

        return repo.findAll(pageable);
    }

    public Order get(Integer id) throws OrderNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new OrderNotFoundException("Cloud not find any orders with ID " + id);
        }
    }

    public List<Country> listCountries() {
        return countryRepo.findAllByOrderByNameAsc();
    }

    public void save(Order order) {
        Order orderInDB = repo.findById(order.getId()).get();

        order.setCustomer(orderInDB.getCustomer());
        order.setOrderTime(orderInDB.getOrderTime());
        order.setOrderTrack(orderInDB.getOrderTrack());

        List<OrderTrack> orderTracks = order.getOrderTrack();

        OrderTrack newTrack = new OrderTrack();

        newTrack.setOrder(order);
        newTrack.setUpdatedTime(new Date());
        newTrack.setNotes(order.getStatus().defaultDescription());
        newTrack.setOrderStatus(order.getStatus());

        orderTracks.add(newTrack);
        repo.save(order);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }

    public void deleteTrack(Integer id, Integer trackId) {
        Order order = repo.findById(id).get();
        Iterator<OrderTrack> iterator = order.getOrderTrack().iterator();

        while (iterator.hasNext()) {
            OrderTrack track = iterator.next();

            if (track.getId() == trackId) {
                iterator.remove();
            }
        }

        repo.save(order);
    }

    public List<Order> ListAll() {
        return (List<Order>) repo.findAll();
    }
}
