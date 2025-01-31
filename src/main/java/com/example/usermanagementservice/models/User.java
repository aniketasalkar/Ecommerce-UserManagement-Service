package com.example.usermanagementservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Fetch;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
//@Table(name = "users")
public class User extends BaseModel {
    @Column(unique = true, nullable = false)
    private String email;

//    @Column(nullable = false)
//    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

//    @ManyToMany(fetch = FetchType.LAZY)
//    private Set<Address> addresses;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserAddress> userAddresses = new HashSet<>();
//    private List<UserRoles> roles;
}
