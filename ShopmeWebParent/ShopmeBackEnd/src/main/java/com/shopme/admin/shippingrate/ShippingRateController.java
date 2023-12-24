package com.shopme.admin.shippingrate;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ShippingRateController {
    @Autowired private ShippingRateService service;


    @GetMapping("/shippingrates")
    public String firstByPage(Model model) {
        return listByPage(1,"asc","country",model,null);
    }

    @GetMapping("/shippingrates/page/{pageNum}")
    private String listByPage(@PathVariable(name = "pageNum") int pageNum,
                              @Param("sortDir") String sortDir,
                              @Param("sortField")String sortField,
                              Model model,
                              @Param("keyword") String keyword) {

        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }

        Page<ShippingRate> page = service.listByPage(pageNum, sortField, sortDir, keyword);
        List<ShippingRate> listShippingRates = page.getContent();

        long startCount = (pageNum - 1) * service.SHIPPINGRATE_PER_PAGE + 1;
        long endCount = startCount + service.SHIPPINGRATE_PER_PAGE - 1;

        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listShippingRates",listShippingRates);
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);


        return "shippingrate/shippingrate";
    }

    @GetMapping("/shippingrates/new")
    public String newShippingRate(Model model) {
        ShippingRate shippingRate = new ShippingRate();
        List<Country> listCountries = service.listCountries();
        model.addAttribute("shippingRate",shippingRate);
        model.addAttribute("listCountries",listCountries);
        return "shippingrate/shippingrate_detail_modal";
    }

    @GetMapping("/shippingrates/detail/{id}")
    public String editShippingRate(Model model,@PathVariable("id")Integer id) {
        ShippingRate shippingRate = null;
        try {
            shippingRate = service.get(id);
            List<Country> listCountries = service.listCountries();
            model.addAttribute("shippingRate",shippingRate);
            model.addAttribute("listCountries",listCountries);
            return "shippingrate/shippingrate_detail_modal";
        } catch (ShippingRateNotFoundException e) {
            return "shippingrate/shippingrate";
        }

    }

    @PostMapping("/shippingrates/save")
    public String newShippingRate(ShippingRate shippingRate,Model model,RedirectAttributes ra) {
        try {
            service.save(shippingRate);
        } catch (ShippingRateAlreadyExistsException e) {
            ra.addFlashAttribute("message",e.getMessage());
        }

        ra.addFlashAttribute("message", "The shipping rate has been saved successfully.");
        return "redirect:/shippingrates";
    }

    @GetMapping("/shippingrates/{id}/enabled/{status}")
    public String updateEnabled(@PathVariable(name = "id")Integer id,
                                RedirectAttributes ra,
                                @PathVariable(name = "status") boolean enabled) {
        service.updateEnabledStatus(id,enabled);
        String status =  enabled ? "enabled" : "disabled";
        String message = "The shipping rate ID " + id + " has been " + status;
        ra.addFlashAttribute("message", message);
        return "redirect:/shippingrates";
    }

    @GetMapping("/shippingrates/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id")Integer id, RedirectAttributes ra) {
        try {
            service.delete(id);
            String message = "The Shipping Rate ID " + id + " has been deleted";
            ra.addFlashAttribute("message",message);
            return "redirect:/shippingrates";
        } catch (ShippingRateNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
            return "redirect:/shippingrates";
        }

    }
}
