package com.shopme.common.entity;

import com.shopme.common.IdBasedEntity;
import jakarta.persistence.*;


import java.util.HashSet;
import java.util.Set;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRoles(Role role) {
        this.roles.add(role);
    }

    @Transient
    public String getPhotosImagePath() {
        if (id == null || photo == null) return "/images/default-user.png";

        return "/user-photos/" + this.id + "/" + this.photo;
    }
}
