package com.shopme.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StateDTO {
    private Integer id;
    private String name;

    public StateDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
