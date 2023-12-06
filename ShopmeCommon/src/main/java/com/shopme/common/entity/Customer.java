package com.shopme.common.entity;

import com.shopme.common.IdBasedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "customers")
public class Customer extends IdBasedEntity {
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
}
