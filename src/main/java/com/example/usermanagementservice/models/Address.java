package com.example.usermanagementservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Address extends BaseModel{
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String zip;

    @Column(nullable = false)
    private String country;
    private String extaNotes;

//    @Column(nullable = false)
//    private Boolean deliveryAddress;

//    @ManyToMany(mappedBy = "addresses")
//    private Set<User> users;

    @ToString.Exclude
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserAddress> userAddresses = new HashSet<>();
}
