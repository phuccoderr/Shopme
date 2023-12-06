package com.shopme.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.shopme.common.IdBasedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "states")
public class State extends IdBasedEntity {
    @Column(length = 45)
    private String name;


    @ManyToOne
    @JoinColumn(name = "country_id")
    @JsonBackReference
    private Country country;
}
