package com.shopme.admin.order;

import com.shopme.admin.country.CountryRepository;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderTrack;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.setting.Setting;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

@Controller
public class OrderController {
    @Autowired private OrderService service;
    @Autowired private SettingService settingService;

    @GetMapping("/orders")
    public String listFirstPage(Model model,HttpServletRequest request) {
        return listByPage(1,"id","asc",null,model,request);
    }
    @GetMapping("/orders/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum")Integer pageNum,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             Model model,HttpServletRequest request) {

        Page<Order> page = service.listByPage(pageNum,sortField,sortDir,keyword);
        List<Order> listOrders = page.getContent();

        long startCount = (pageNum - 1) * service.ORDER_PER_PAGE + 1;
        long endCount = startCount + service.ORDER_PER_PAGE - 1;

        String reverseSortDir = sortDir.equals("asc") ? "dec" : "asc";
        model.addAttribute("listOrders",listOrders);
        //pagination
        model.addAttribute("currentPage",pageNum); //page hien tai
        model.addAttribute("totalPages",page.getTotalPages()); //tong so trang
        model.addAttribute("startCount",startCount); //trang bat dau
        model.addAttribute("endCount",endCount); //trang ket thuc
        model.addAttribute("totalItems",page.getTotalElements());

        //param
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("reverseSortDir", reverseSortDir);
        loadCurrencySetting(request);
        return "order/order";
    }

    private void loadCurrencySetting(HttpServletRequest request) {
        List<Setting> settings = settingService.getCurrencySettings();
        settings.forEach(setting -> {
            request.setAttribute(setting.getKey(), setting.getValue());
        });
    }

    @GetMapping("/orders/detail/{id}")
    public String editOrder(@PathVariable("id") Integer id,Model model,
                            RedirectAttributes ra,
                            HttpServletRequest request) {
        Order order = null;
        try {
            order = service.get(id);
            List<Country> listCountries = service.listCountries();
            model.addAttribute("order",order);
            model.addAttribute("listCountries",listCountries);
            loadCurrencySetting(request);
            return "order/order_form_modal";
        } catch (OrderNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
            return "order/order";
        }



    }

    @PostMapping("/orders/save")
    public String saveOrder(Model model,Order order,HttpServletRequest request,RedirectAttributes ra) {
        float shippingCost = Float.parseFloat(request.getParameter("shippingCost"));
        float productCost = Float.parseFloat(request.getParameter("productCost"));
        float subTotal = Float.parseFloat(request.getParameter("subTotal"));
        float total = Float.parseFloat(request.getParameter("total"));
        String countryName = request.getParameter("countryName");

        order.setShippingCost(shippingCost);
        order.setSubtotal(subTotal);
        order.setTotal(total);
        order.setCountry(countryName);
        order.setProductCost(productCost);


        updateProductDetail(order,request);

        service.save(order);
        ra.addFlashAttribute("message","Order has been update succesfully with ID:" + order.getId());
        return "redirect:/orders";
    }



    private void updateProductDetail(Order order, HttpServletRequest request) {

        String[] detailIds = request.getParameterValues("detailId");
        String[] productIds = request.getParameterValues("productId");
        String[] productCosts = request.getParameterValues("productCost");
        String[] quantitys = request.getParameterValues("quantity");
        String[] productPrices = request.getParameterValues("productPrice");
        String[] productSubtotals = request.getParameterValues("productSubtotal");
        String[] productShipCosts = request.getParameterValues("productShipCost");
        Set<OrderDetail> orderDetails = order.getOrderDetails();

        for (int i = 0;i < detailIds.length;i++) {

            OrderDetail orderDetail = new OrderDetail();
            Integer detailId = Integer.parseInt(detailIds[i]);
            if (detailId > 0) {
                orderDetail.setId(detailId);
            }

            orderDetail.setOrder(order);
            orderDetail.setProduct(new Product(Integer.parseInt(productIds[i])));
            orderDetail.setQuantity(Integer.parseInt(quantitys[i]));
            orderDetail.setSubtotal(Float.parseFloat(productSubtotals[i]));
            orderDetail.setShippingCost(Float.parseFloat(productShipCosts[i]));
            orderDetail.setUnitPrice(Float.parseFloat(productPrices[i]));
            orderDetail.setProductCost(Float.parseFloat(productCosts[i]));

            orderDetails.add(orderDetail);
        }

    }

    @GetMapping("/orders/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("message","Order has been delete succesfully with ID:" + id);
        return "redirect:/orders";
    }

    @GetMapping("/orders/{orderId}/track/{trackId}")
    public String deleteTrack(@PathVariable("orderId") Integer orderId,@PathVariable("trackId") Integer trackId,
                            RedirectAttributes ra) {
        service.deleteTrack(orderId,trackId);
        ra.addFlashAttribute("message","Order has been update succesfully with ID:" + orderId);
        return "redirect:/orders";
    }
}
