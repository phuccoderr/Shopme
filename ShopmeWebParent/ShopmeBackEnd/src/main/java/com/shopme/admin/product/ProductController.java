package com.shopme.admin.product;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ProductController {
    @Autowired private ProductService service;
    @Autowired private BrandService brandService;
    @Autowired private CategoryService categoryService;

    @GetMapping("/products")
    public String listFirstPage(Model model) {
        return listByPage(1,"asc",null,0,model);
    }
    @GetMapping("/products/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             @Param("categoryId") Integer categoryId, Model model) {
        Page<Product> page = service.listByPage(pageNum,sortDir,keyword,categoryId);
        List<Product> listProducts = page.getContent();
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        List<Category> listCategories = categoryService.listCategoriesInForm();

        long startCount = (pageNum - 1) * service.PRODUCTS_PER_PAGE + 1;
        long endCount = startCount + service.PRODUCTS_PER_PAGE - 1;

        if(categoryId != null) model.addAttribute("categoryId", categoryId);

        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("sortField","name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listProducts",listProducts);
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);
        model.addAttribute("listCategories", listCategories);

        return "product/product";

    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        Product product = new Product();
        List<Brand> listBrands = brandService.listBrand();
        model.addAttribute("listBrands",listBrands);
        model.addAttribute("product",product);

        Integer numberOfExistingExtraImages = product.getProductImages().size();
        model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
        return "product/product_form_modal";
    }

    @GetMapping("/products/detail/{id}")
    public String editProduct(Model model, @PathVariable(name = "id") Integer id,RedirectAttributes ra) {
        Product product = null;
        try {
            product = service.get(id);
            List<Brand> listBrands = brandService.listBrand();
            model.addAttribute("listBrands",listBrands);
            model.addAttribute("product",product);

            Integer numberOfExistingExtraImages = product.getProductImages().size();
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
            return "product/product_form_modal";
        } catch (ProductNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
            return "product/product";
        }

    }

    @PostMapping("/products/save")
    public String saveProduct(Model model,
                              @RequestParam("fileImage")MultipartFile mainMultipartFile,
                              @RequestParam("extraImage") MultipartFile[] extraMultipartFiles,
                              @RequestParam(name = "detailIDs",required = false) String[] detailIds,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false)String[] detailValues,
                              @RequestParam(name = "imageIDs", required = false)String[] imageIDs,
                              @RequestParam(name = "imageNames", required = false)String[] imageNames,
                              RedirectAttributes ra,
                              Product product) throws IOException {
        //set mainImage
        setMainImageName(product,mainMultipartFile);
        //set lai file ProductImage cu,tham chieu productImage de tranh thuoc tinh orphanRemoval
        setExistingExtraImageNames(imageIDs,imageNames,product);
        //nhan extraImage them moi hoac sua
        setNewExtraImageNames(product,extraMultipartFiles);
        setProductDetails(product,detailIds,detailNames,detailValues);

        Product saved = service.save(product);

        saveUploadImages(mainMultipartFile,extraMultipartFiles,saved);
        //xoa file neu no k thay file tham chieu
        deleteExtraImageWeredRemovedOnForm(product);


        ra.addFlashAttribute("message","The product has been saved successfully.");
        return "redirect:/products";
    }

    private void deleteExtraImageWeredRemovedOnForm(Product product) throws IOException {
        String extraImage = "product-images/" + product.getId() + "/extras";
        Path dirpath = Paths.get(extraImage);

        try {
            Files.list(dirpath).forEach(file -> {
                String fileName = file.toFile().getName();

                if (!product.containsImageName(fileName)) {
                    try {
                        System.out.println("file delete: " + fileName);
                        Files.delete(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (IOException e) {
            throw new IOException("could not list directory: " + dirpath);
        }
    }

    private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
        if (imageIDs == null || imageIDs.length == 0) return;
        Set<ProductImage> images = new HashSet<>();
        for (int count = 0; count < imageIDs.length;count++) {
            Integer id = Integer.parseInt(imageIDs[count]);
            String name = imageNames[count];
            images.add(new ProductImage(id,name,product));

        }
        product.setProductImages(images);
    }


    private void setProductDetails(Product product, String[] detailID,
                                   String[] detailNames, String[] detailValues) {
        if (detailNames == null || detailValues.length == 0) return;
        for (int i = 0; i < detailNames.length; i++) {
            String name = detailNames[i];
            String value = detailValues[i];
            Integer id = Integer.parseInt(detailID[i]);

            if(id != 0) {
                product.addDetail(id,name,value);
            } else if (!name.isEmpty() && !value.isEmpty()) {
                product.addDetail(name,value);
            }
        }
    }


    private void setMainImageName(Product product, MultipartFile mainMultipartFile) {
        if (!mainMultipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainMultipartFile.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }
    private void setNewExtraImageNames(Product product, MultipartFile[] extraMultipartFiles) {
        if (extraMultipartFiles.length > 0) {
            for (MultipartFile multipartFile : extraMultipartFiles) {
                if (!multipartFile.isEmpty()) {

                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    if (!product.containsImageName(fileName)) {
                        product.addExtraImage(fileName);
                    }
                }
            }
        }

    }

    private void saveUploadImages(MultipartFile mainMultipartFile,
                                  MultipartFile[] extraMultipartFiles,
                                  Product savedProduct) throws IOException {
        if (!mainMultipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainMultipartFile.getOriginalFilename());
            String uploadDir = "product-images/" + savedProduct.getId() + "/";

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,mainMultipartFile);

        }

        if(extraMultipartFiles.length > 0) {
            String uploadDir = "product-images/" + savedProduct.getId() + "/extras";
            for (MultipartFile multipartFile : extraMultipartFiles) {

                if(!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
                }
            }
        }
    }
    @GetMapping("/products/{id}/enabled/{status}")
    public String updateEnabled(@PathVariable(name = "id")Integer id,
                                RedirectAttributes ra,
                                @PathVariable(name = "status") boolean enabled) {
        service.updateEnabled(id,enabled);
        String status =  enabled ? "enabled" : "disabled";
        String message = "The product ID " + id + " has been " + status;
        ra.addFlashAttribute("message", message);
        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id")Integer id,
                                RedirectAttributes ra) {
        try {
            service.delete(id);
            String message = "The product ID " + id + " has been delete";
            ra.addFlashAttribute("message", message);
            return "redirect:/products";
        } catch (ProductNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/products";
        }

    }
}
