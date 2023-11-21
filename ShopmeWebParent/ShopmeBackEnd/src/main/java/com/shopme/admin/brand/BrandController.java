package com.shopme.admin.brand;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
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
public class BrandController {

    @Autowired private BrandService service;
    @Autowired private CategoryService categoryService;

    @GetMapping("/brands")
    public String listFirstPage(Model model) {
        return listByPage(1,"id","asc",null,model);
    }

    @GetMapping("/brands/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             Model model) {
        Page<Brand> page = service.listByPage(pageNum,sortField,sortDir,keyword);
        List<Brand> listBrands = page.getContent();

        long startCount = (pageNum - 1) * service.BRAND_PER_PAGE + 1;
        long endCount = startCount + service.BRAND_PER_PAGE - 1;


        model.addAttribute("listBrands",listBrands);
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

        return "brand/brand";
    }

    @GetMapping("/brands/new")
    public String newBrand(Model model) {
        Brand brand = new Brand();
        List<Category> listCategories = categoryService.listCategoriesInForm();
        model.addAttribute("listCategories",listCategories);
        model.addAttribute("brand",brand);

        return "brand/brand_detail_modal";
    }

    @PostMapping("/brands/save")
    public String saveBrand(Model model,
                            @RequestParam("fileImage")MultipartFile multipartFile,
                            Brand brand,
                            RedirectAttributes ra) throws IOException {
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);
            Brand saved = service.save(brand);

            String uploadDir = "brand-logos/" + saved.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        } else {
            service.save(brand);
        }
        ra.addFlashAttribute("message","The brand has been saved successfully.");
        return "redirect:/brands";
    }

    @GetMapping("/brands/detail/{id}")
    public String editDetailBrand(@PathVariable(name = "id")Integer id, Model model, RedirectAttributes ra) {
        try {
            Brand brand = service.get(id);
            List<Category> listCategories = categoryService.listCategoriesInForm();
            model.addAttribute("listCategories",listCategories);
            model.addAttribute("brand",brand);
            return "brand/brand_detail_modal";
        } catch (BrandNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
            return "redirect:/brands";
        }
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id")Integer id, RedirectAttributes ra) {
        service.delete(id);
        String message = "The brand ID " + id + " has been deleted";
        ra.addFlashAttribute("message",message);
        return "redirect:/brands";
    }
}
