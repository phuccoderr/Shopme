package com.shopme.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shopme.common.IdBasedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends IdBasedEntity implements Comparable<Category>{
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
    @JsonBackReference
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Category> children = new HashSet<>();

    @ManyToMany(mappedBy = "categories")
    private Set<Brand> brands = new HashSet<>();

    public Category(Integer id) {
        this.id = id;
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




    @Transient
    private boolean hasChildren;

    @Transient
    public String getImagePath() {
        if (this.id == null ) return "/images/default-user.png";
        if (this.img.isEmpty())   return "/images/default-user.png";

        return "/category-images/" + this.id + "/" + this.img;
    }

    @Override
    public int compareTo(Category category) {
        return this.name.compareTo(category.getName());
    }
}
