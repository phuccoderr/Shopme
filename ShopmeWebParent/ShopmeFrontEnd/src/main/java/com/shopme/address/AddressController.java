package com.shopme.address;

import com.shopme.Utility;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AddressController {
    @Autowired private AddressService service;
    @Autowired private CustomerService customerService;

    @GetMapping("/address")
    public String listByCustomer(HttpServletRequest request,Model model) {
        Customer customer = getAuthenticatedCustomer(request);
        List<Address> listAddresses = service.listByCustomer(customer);

        boolean defaultAddress = false;

        for (Address address : listAddresses) {
            if (address.isDefaultAddress()) {
                defaultAddress = true;
                break;
            }
        }


        model.addAttribute("defaultAddress",defaultAddress);
        model.addAttribute("customer",customer);

        model.addAttribute("listAddresses",listAddresses);
        return "address_book/address";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String customer = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(customer);
    }

    @GetMapping("/address_book/new")
    public String newAddress(Model model,HttpServletRequest request) {
        List<Country> listCountries = customerService.listCountries();
        Address address = new Address();

        model.addAttribute("address",address);
        model.addAttribute("listCountries",listCountries);
        return "address_book/address_form";

    }

    @PostMapping("/address_book/save")
    public String saveAddress(Address address, HttpServletRequest request, RedirectAttributes ra) {
        Customer customer = getAuthenticatedCustomer(request);
        service.save(address,customer);
        ra.addFlashAttribute("message","The address has been saved successfully.");

        return "redirect:/address";
    }

    @GetMapping("/address_book/edit/{id}")
    public String editAddress(@PathVariable("id")Integer id,Model model,HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);

        List<Country> listCountries = customerService.listCountries();
        Address address = service.get(id,customer.getId());

        model.addAttribute("address",address);
        model.addAttribute("listCountries",listCountries);
        return "address_book/address_form";
    }

    @GetMapping("/address_book/default/{id}")
    public String setDefaultAddress(@PathVariable("id")Integer id,HttpServletRequest request,RedirectAttributes ra) {
        Customer customer = getAuthenticatedCustomer(request);

        String redirect = request.getParameter("redirect");
        String redirectURL = "redirect:/address";
        ra.addFlashAttribute("message","The address has been saved successfully.");


        service.updateDefaultAddressByCustomer(id,customer.getId());

        return redirectURL;
    }

    @GetMapping("/address_book/delete/{id}")
    public String deleteAddress(@PathVariable("id")Integer id,HttpServletRequest request,RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("message","The address " + id + " has been delete successfully.");

        return "redirect:/address";
    }


}
