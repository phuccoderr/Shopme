package com.shopme.common.entity;

import com.shopme.common.Constants;
import com.shopme.common.IdBasedEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "brands")
public class Brand extends IdBasedEntity implements Comparable<Brand> {
    @Column(length = 40, nullable = false,unique = true)
    private String name;
    @Column(length = 128,nullable = false)
    private String logo;

    @ManyToMany
    @JoinTable(
            name = "brands_categories",
            joinColumns = @JoinColumn(name = "brand_id"), // bang entity hien tai
            inverseJoinColumns = @JoinColumn(name = "category_id") //bang ke tiep
    )
    private Set<Category> categories = new HashSet<>();



    public Brand(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }



    @Transient
    public String getLogoPath() {
        if (this.id == null || this.logo.isEmpty() ) return Constants.S3_BASE_URI + "/images/default-user.png";
        return Constants.S3_BASE_URI + "/brand-logos/" + this.id +  "/" + this.logo;
    }

    @Override
    public int compareTo(Brand brand) {
        return this.name.compareTo(brand.getName());
    }
}
