package com.shopme.admin.customer;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired private CustomerService service;

    @GetMapping("/customers")
    public String firstByPage(Model model) {
        return listByPage(1,"id","asc",null,model);

    }
    @GetMapping("/customers/page/{pageNum}")
    private String listByPage(@PathVariable(name = "pageNum") int pageNum,
                              @Param("sortField") String sortField,
                              @Param("sortDir") String sortDir,
                              @Param("keyword") String keyword,
                              Model model) {
        Page<Customer> page = service.listByPage(pageNum,sortField,sortDir,keyword);
        List<Customer> listCustomers = page.getContent();

        long startCount = (pageNum - 1) * service.CUSTOMER_PER_PAGE + 1;
        long endCount = startCount + service.CUSTOMER_PER_PAGE - 1;


        model.addAttribute("listCustomers",listCustomers);
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

        return "customer/customer";
    }

    @GetMapping("/customers/detail/{id}")
    public String editDetailBrand(@PathVariable(name = "id")Integer id, Model model, RedirectAttributes ra) {
        try {
            Customer customer = service.get(id);
            model.addAttribute("customer",customer);
            return "customer/customer_detail_modal";
        } catch (CustomerNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
            return "redirect:/customers";
        }
    }

    @PostMapping("/customers/save")
    public String saveBrand(Model model,
                            Customer customer,
                            RedirectAttributes ra) throws IOException {

        service.save(customer);
        ra.addFlashAttribute("message","The Customer has been saved successfully.");
        return "redirect:/customers";
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id")Integer id, RedirectAttributes ra) {
        service.delete(id);
        String message = "The Customer ID " + id + " has been deleted";
        ra.addFlashAttribute("message",message);
        return "redirect:/customers";
    }
}
