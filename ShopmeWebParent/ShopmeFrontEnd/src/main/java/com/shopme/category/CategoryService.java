package com.shopme.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired private CategoryRepository repo;

    public List<Category> listCategoryNoParent () {
        return repo.findAllEnabledNoParen();
    }

    public Category getCategory(String alias){
        return repo.findByAliasEnabled(alias);
    }

    public List<Category> getCategoryParents(Category child) {
        List<Category> listParents = new ArrayList<>();
        Category parent = child.getParent();

        while(parent != null) {
            listParents.add(0,parent);
            parent = parent.getParent();
        }
        listParents.add(child);
        return listParents;
    }


}
