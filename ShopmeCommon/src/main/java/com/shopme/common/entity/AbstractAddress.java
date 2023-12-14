package com.shopme.common.entity;

import com.shopme.common.IdBasedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Setter
@Getter
public class AbstractAddress extends IdBasedEntity {
    @Column(name = "first_name",nullable = false,length = 45)
    protected String firstName;

    @Column(name = "last_name",length = 45)
    protected String lastName;

    @Column(name = "phone_number",length = 15)
    protected String phoneNumber;

    @Column(name = "address_line1",length = 64)
    protected String addressLine1;
    @Column(name = "address_line2",length = 64)
    protected String addressLine2;

    @Column(length = 45)
    protected String city;
    @Column(length = 45)
    protected String state;
    @Column(name = "postal_code", length = 10)
    protected String postalCode;
}
