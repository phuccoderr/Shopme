package com.shopme.admin.product;

import com.shopme.common.entity.product.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ProductService {
    public static final int PRODUCTS_PER_PAGE = 10;
    @Autowired private ProductRepository repo;

    public List<Product> listAll () {
        return (List<Product>) repo.findAll();
    }

    public Page<Product> listByPage(int pageNum,String dir,String keyword ,Integer categoryId) {
        Sort sort = Sort.by("id");
        sort = dir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1 , PRODUCTS_PER_PAGE,sort);
        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
                return repo.searchInCategory(categoryId,categoryIdMatch,keyword,pageable);
            }
            return repo.findAll(keyword,pageable);
        }

        if (categoryId != null && categoryId > 0) {
            String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
            return repo.findAllCategory(categoryId, categoryIdMatch, pageable);
        }

        return repo.findAll(pageable);
    }

    public Product get(Integer id) {
        return repo.findById(id).get();
    }

    public String checkUnique(Integer id, String name) {
        boolean isProductNew = (id == null || id == 0);
        Product product = repo.findByName(name);
        if (isProductNew) {
            if(product != null) {
                return "Duplicated";
            }
        } else {
            if(product != null && product.getId() != id) {
                return "Duplicated";
            }
        }
        return "OK";
    }

    public Product save(Product product) {
        boolean isProduct = product.getId() == null;
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }
        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String replace = product.getName().replace(" ","-");
            String defaultAlias = replace.replace("/","-");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replace(" ","-"));
        }
        //kiem tra mainImage
        if(isProduct) {
            if (product.getMainImage().isEmpty()) {
                product.setMainImage("");
            }
        } else {
            if (product.getMainImage() == null) {
                Product productInDB = repo.findById(product.getId()).get();
                product.setMainImage(productInDB.getMainImage());
            }
        }
        product.setUpdatedTime(new Date());

        return repo.save(product);
    }

    public void updateEnabled(Integer id,boolean enabled) {
        repo.enabled(id,enabled);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
