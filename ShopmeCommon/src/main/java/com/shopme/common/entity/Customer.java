package com.shopme.common.entity;

import com.shopme.common.IdBasedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "customers")
public class Customer extends AbstractAddress {
    @Column(nullable = false,length = 45)
    private String email;
    @Column(nullable = false,length = 64)
    private String password;

    @Column(name = "created_time",nullable = false)
    private Date createdTime;

    private boolean enabled;

    @Column(name = "verification_code",length = 64)
    private String verificationCode;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", length = 10)
    private AuthenticationType authenticationType;

    @Column(name = "reset_password_token",length = 30)
    private String resetPasswordToken;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();

    public Customer(Integer id) {
        this.id = id;
    }

    @Transient
    public String getAddress() {
        String address = firstName;

        if (lastName != null && !lastName.isEmpty()) address += " " + lastName;

        if (!addressLine1.isEmpty()) address += ", " + addressLine1;

        if (addressLine2 != null && !addressLine2.isEmpty() ) address += ", " + addressLine1;

        if (!city.isEmpty()) address += ", " + city;

        if (state != null && !state.isEmpty()) address += " " + state;

        if (!postalCode.isEmpty()) address += ". Postal Code:" + postalCode;

        if (!phoneNumber.isEmpty()) address += ". Phone Number:" + phoneNumber + ", ";

        address += country.getName();

        return address;
    }

    @Transient
    public String getFullName() {
        return  this.firstName + " " + this.lastName;
    }
}
