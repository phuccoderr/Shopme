package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {
    public static final int BRAND_PER_PAGE = 10;
    @Autowired private BrandRepository repo;

    public List<Brand> listBrand () {
        return (List<Brand>) repo.findAll();
    }

    public Page<Brand> listByPage(int pageNum,String sortField, String sortDir,String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1,BRAND_PER_PAGE,sort);

        if (keyword != null) {
            return repo.findAll(keyword,pageable);
        }
        return repo.findAll(pageable);

    }

    public Brand save(Brand brand) {

        boolean haveLogo = brand.getId() == null;

        if ( brand.getLogo() == null) {
            brand.setLogo("");
            if (haveLogo) {
                brand.setLogo("");
            } else {
                Brand brandInDB = repo.findByName(brand.getName());
                brand.setLogo(brandInDB.getLogo());
            }
        }
        return repo.save(brand);
    }

    public Brand get(Integer id) throws BrandNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new BrandNotFoundException("Cloud any brand with id: " + id);
        }
    }

    public void delete(Integer id) throws BrandNotFoundException {
        Long countById = repo.countById(id);
        if (countById == null || countById ==0) {
            throw new BrandNotFoundException("Cloud not find any category  with ID:" + id);
        }
        repo.deleteById(id);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id ==0);
        Brand brand = repo.findByName(name);
        if(isCreatingNew) {
            if (brand != null) {
                return "DuplicatedName";
            }
        } else {
            if (brand != null && brand.getId() != id) {
                return "DuplicatedName";
            }
        }

        return "OK";
    }
}
