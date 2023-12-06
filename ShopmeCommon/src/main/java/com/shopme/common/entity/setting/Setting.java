package com.shopme.common.entity.setting;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "settings")
public class Setting {
    @Id
    @Column(name = "`key`",nullable = false,length = 128)
    private String key;
    @Column(nullable = false,length = 1024)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(length = 45,nullable = false)
    private SettingCategory category;

    public Setting (String key) {
        this.key = key;
    }

    public Setting(String key, String value, SettingCategory category) {
        this.key = key;
        this.value = value;
        this.category = category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Setting other = (Setting) obj;
        return Objects.equals(key, other.key);
    }
}
