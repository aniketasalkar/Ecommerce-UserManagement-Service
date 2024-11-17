package com.example.usermanagementservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Fetch;

import java.util.List;
import java.util.Set;

@Data
@Entity
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

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Address> addresses;
//    private List<UserRoles> roles;
}
