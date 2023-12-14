package com.shopme.common.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shopme.common.IdBasedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "countries")
public class Country extends IdBasedEntity {
    @Column(nullable = false,length = 45)
    private String name;
    @Column(nullable = false,length = 5)
    private String code;

    @OneToMany(mappedBy = "country", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<State> states;

    public Country(Integer id) {
        this.id = id;
    }

    public Country(String countryName, String countryCode) {
        this.name = countryName;
        this.code = countryCode;
    }

    public Country(Integer countryId, String countryName, String countryCode) {
        this.id = countryId;
        this.name = countryName;
        this.code = countryCode;
    }
}
