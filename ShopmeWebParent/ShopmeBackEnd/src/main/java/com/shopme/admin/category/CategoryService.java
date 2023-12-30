package com.shopme.admin.category;


import com.shopme.common.entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class CategoryService {
    public static final int CATEGORY_PER_PAGE = 10;
    @Autowired private CategoryRepository repo;


    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortDir,String keyword) {

        Sort sort = Sort.by("name");
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1,CATEGORY_PER_PAGE,sort);


        Page<Category> pageCategories = null;
        if (keyword != null && !keyword.isEmpty()) {
            pageCategories = repo.search(keyword,pageable);
        } else {
            pageCategories = repo.findRootCategories(pageable);
        }
        pageInfo.setTotalElements(pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());
        //Category muc 0 hoac category da tim duoc
        List<Category> rootCategories = pageCategories.getContent();

        if (keyword != null && !keyword.isEmpty()) {
            for (Category category : rootCategories) {
                category.setHasChildren(category.getChildren().size() > 0);
            }
            return rootCategories;
        } else {
            return listHierarchicalCategories(rootCategories);
        }


    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for(Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));
            Set<Category> children = rootCategory.getChildren();

            // Sort the children set based on Category name
            List<Category> sortedChildren = new ArrayList<>(children);
            Collections.sort(sortedChildren, Comparator.comparing(Category::getName));

            for (Category subCategory : sortedChildren) {

                String name = "--" + subCategory.getName();

                hierarchicalCategories.add(Category.copyFull(subCategory,name));

                subListHierarchicalCategories(hierarchicalCategories,subCategory,1);
            }
        }

        return hierarchicalCategories;
    }

    private void subListHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int level) {
        Set<Category> children = parent.getChildren();
        level++;

        // Sort the children set based on Category name
        List<Category> sortedChildren = new ArrayList<>(children);
        Collections.sort(sortedChildren, Comparator.comparing(Category::getName));

        for (Category subCategory : sortedChildren) {
            String name = "";
            for (int i = 0; i < level; i++) {
                name += "--";
            }
            name += subCategory.getName();
            hierarchicalCategories.add(Category.copyFull(subCategory,name));

            subListHierarchicalCategories(hierarchicalCategories,subCategory,level);
        }
    }


    public Category save(Category category) {
        Category parent = category.getParent();

        boolean haveImage = category.getId() == null;

        if (parent != null) {
            String allParentIds = parent.getAllParentIds() == null ? "-" : parent.getAllParentIds();
            allParentIds += String.valueOf(parent.getId()) + "-";
            category.setAllParentIds(allParentIds);
        }
        if ( category.getImg() == null) {
            category.setImg("");
            if (haveImage) {
                category.setImg("");
            } else {
                Category categoryInDB = repo.findByName(category.getName());
                category.setImg(categoryInDB.getImg());
            }
        }
        return repo.save(category);
    }

    public Category get(Integer id) {
        return repo.findById(id).get();
    }

    public List<Category> listCategoriesInForm() {
        List<Category> categoriesInForm = new ArrayList<>();
        List<Category> categoriesInDB = repo.findRootCategories(Sort.by("name").ascending());

        for (Category category : categoriesInDB) {

            categoriesInForm.add(Category.copyFull(category));
            Set<Category> children = category.getChildren();

            // Sort the children set based on Category name
            List<Category> sortedChildren = new ArrayList<>(children);
            Collections.sort(sortedChildren, Comparator.comparing(Category::getName));

            for(Category subCategory : sortedChildren) {
                String name = "--" + subCategory.getName();

                categoriesInForm.add(Category.copyIdAndName(subCategory.getId(),name));
                subListHierarchicalCategoriesInForm(categoriesInForm,subCategory,1);
            }
        }
        return categoriesInForm;
    }
    private void subListHierarchicalCategoriesInForm(List<Category> hierarchicalCategories, Category parent,int level) {
        Set<Category> children = parent.getChildren();
        level++;

        // Sort the children set based on Category name
        List<Category> sortedChildren = new ArrayList<>(children);
        Collections.sort(sortedChildren, Comparator.comparing(Category::getName));

        for (Category subCategory : sortedChildren) {
            String name = "";
            for (int i = 0; i < level;i++) {
                name += "--" ;
            }
            name += subCategory.getName();

            hierarchicalCategories.add(Category.copyIdAndName(subCategory.getId(),name));

            subListHierarchicalCategoriesInForm(hierarchicalCategories,subCategory,level);


        }
    }


    public void delete(Integer id) throws CategoryNotFoundException {
        Category category = repo.findById(id).get();
        if (category == null || category.getId() == 0) {
            throw new CategoryNotFoundException("Cloud not find any category with ID: " + id);
        }
        repo.deleteById(id);
    }

    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);
        Category categoryByName = repo.findByName(name);
        if (isCreatingNew) {
            if (categoryByName != null) {
                return "DuplicatedName";
            } else {
                Category categoryByAlias = repo.findByAlias(alias);
                if (categoryByAlias != null) {
                    return "DuplicatedAlias";
                }
            }
        } else {
            if (categoryByName != null && categoryByName.getId() != id) {
                return "DuplicatedName";
            }
            Category categoryByAlias = repo.findByAlias(alias);
            if (categoryByAlias != null && categoryByAlias.getId() != id) {
                return "DuplicatedAlias";
            }
        }
        return "OK";
    }
    public void updateEnabledStatus(Integer id,boolean enabled) {
        repo.updateEnabledStatus(id, enabled);
    }

}
