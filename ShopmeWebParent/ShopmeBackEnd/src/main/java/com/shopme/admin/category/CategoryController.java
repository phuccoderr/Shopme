package com.shopme.admin.category;

import com.shopme.admin.AmazonS3Util;
import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CategoryController {
    @Autowired private CategoryService service;
    @Autowired private BrandService brandService;

    @GetMapping("/categories")
    public String listFirstPage(Model model) {
        return listByPage(1,"asc",null,model);
    }
    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             Model model) {

        CategoryPageInfo pageInfo = new CategoryPageInfo();
        List<Category> listCategories = service.listByPage(pageInfo,pageNum,sortDir,keyword);

        long startCount = (pageNum - 1) * service.CATEGORY_PER_PAGE + 1;
        long endCount = startCount + service.CATEGORY_PER_PAGE - 1;

        if (endCount > pageInfo.getTotalElements()) {
            endCount = pageInfo.getTotalElements();
        }

        model.addAttribute("listCategories",listCategories);

        //pagination
        model.addAttribute("currentPage",pageNum); //page hien tai
        model.addAttribute("totalPages",pageInfo.getTotalPages()); //tong so trang
        model.addAttribute("startCount",startCount); //trang bat dau
        model.addAttribute("endCount",endCount); //trang ket thuc
        model.addAttribute("totalItems",pageInfo.getTotalElements());

        //param
        model.addAttribute("sortField","name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);

        return "category/category";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        Category category = new Category();
        List<Category> listCategoriesUsed = service.listCategoriesInForm();
        model.addAttribute("category",category);
        model.addAttribute("listCategoriesUsed",listCategoriesUsed);

        return "category/category_detail_modal";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Model model,
                               @RequestParam("fileImage")MultipartFile multipartFile,
                               Category category,
                               RedirectAttributes ra) throws IOException {
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImg(fileName);
            Category saved = service.save(category);
            String uploadDir = "category-images/" + saved.getId();

//            FileUploadUtil.cleanDir(uploadDir);
//            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir,fileName,multipartFile.getInputStream());
        } else {
            service.save(category);
        }

        ra.addFlashAttribute("message","The brand has been saved successfully.");
        return "redirect:/categories";
    }

    @GetMapping("/categories/detail/{id}")
    public String editDetailCate(@PathVariable(name = "id") Integer id,
                                 Model model) {
        Category category = service.get(id);
        model.addAttribute("category",category);
        List<Category> listCategoriesUsed = service.listCategoriesInForm();
        model.addAttribute("listCategoriesUsed",listCategoriesUsed);
        return "category/category_detail_modal";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCate(@PathVariable(name = "id") Integer id,RedirectAttributes ra) {
        try {
            service.delete(id);
            String message = "The category ID " + id + " has been deleted";
            ra.addFlashAttribute("message",message);
        } catch (CategoryNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
        }
        return "redirect:/categories";
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled,
                                              RedirectAttributes redirectAttributes) {
        service.updateEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The category ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/categories";
    }
}
