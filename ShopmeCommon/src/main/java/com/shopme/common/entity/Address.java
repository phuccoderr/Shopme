package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "addresses")
public class Address extends AbstractAddress{
    @Column(name = "default_address")
    private boolean defaultAddress;

    @ManyToOne
    @JoinColumn(name = "country_id" )
    private Country country;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Override
    public String toString() {
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
}
