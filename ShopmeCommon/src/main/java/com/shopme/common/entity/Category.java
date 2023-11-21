package com.shopme.common.entity;

import com.shopme.common.IdBasedEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category extends IdBasedEntity {
    @Column(length = 128, nullable = false, unique = true)
    private String name;
    @Column(length = 64, nullable = false, unique = true)
    private String alias;
    @Column( nullable = false)
    private String img;
    private boolean enabled;
    @Column(name = "all_parent_ids",length = 256,nullable = true)
    private String allParentIds;


    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Category> children = new HashSet<>();




    public Category() {
    }

    public static Category copyFull(Category category) {
        Category copyCategory = new Category();
        copyCategory.setId(category.getId());
        copyCategory.setName(category.getName());
        copyCategory.setImg(category.getImg());
        copyCategory.setAlias(category.getAlias());
        copyCategory.setEnabled(category.isEnabled());
        return  copyCategory;

    }

    public static Category copyIdAndName(Integer id,String name) {
        Category copyCategory = new Category();
        copyCategory.setId(id);
        copyCategory.setName(name);
        return  copyCategory;

    }

    public static Category copyFull(Category category,String name) {
        Category copyCategory = Category.copyFull(category);
        copyCategory.setName(name);
        return  copyCategory;

    }

    public Category(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAllParentIds() {
        return allParentIds;
    }

    public void setAllParentIds(String allParentIds) {
        this.allParentIds = allParentIds;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    @Transient
    private boolean hasChildren;

    @Transient
    public String getImagePath() {
        if (this.id == null ) return "/images/default-user.png";
        if (this.img.isEmpty())   return "/images/default-user.png";

        return "/category-images/" + this.id + "/" + this.img;
    }

}
