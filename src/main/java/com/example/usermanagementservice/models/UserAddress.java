package com.example.usermanagementservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(callSuper = true, exclude = {"user", "address"})
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "address_id"})
})
public class UserAddress extends BaseModel {
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(nullable = false)
    private Boolean deliveryAddress = false;
}
