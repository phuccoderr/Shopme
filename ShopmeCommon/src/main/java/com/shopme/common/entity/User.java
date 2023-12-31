package com.shopme.common.entity;

import com.shopme.common.IdBasedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends IdBasedEntity {
    @Column(length = 128,nullable = false)
    private String email;
    private boolean enabled;
    @Column(length = 64,nullable = false)
    private String password;
    @Column(name = "first_name",length = 64,nullable = false)
    private String firstName;
    @Column(name = "last_name",length = 64,nullable = false)
    private String lastName;
    @Column(length = 64)
    private String photo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns =  @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    public void addRoles(Role role) {
        this.roles.add(role);
    }


    @Transient
    public String getPhotosImagePath() {
        if (id == null || photo == null) return "/images/default-user.png";

        return "/user-photos/" + this.id + "/" + this.photo;
    }
}
