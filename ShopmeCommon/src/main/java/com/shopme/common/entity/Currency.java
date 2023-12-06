package com.shopme.common.entity;

import com.shopme.common.IdBasedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "currency")
public class Currency extends IdBasedEntity {
    @Column(nullable = false,length = 64)
    private String name;
    @Column(nullable = false,length = 3)
    private String symbol;
    @Column(nullable = false,length = 4)
    private String code;

    public Currency(String name, String symbol, String code) {
        this.name = name;
        this.symbol = symbol;
        this.code = code;
    }

    @Override
    public String toString() {
        return name + " - " + code + " - " + symbol;
    }
}
