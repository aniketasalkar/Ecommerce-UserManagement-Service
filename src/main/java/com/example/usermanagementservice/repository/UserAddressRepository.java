package com.example.usermanagementservice.repository;

import com.example.usermanagementservice.models.Address;
import com.example.usermanagementservice.models.User;
import com.example.usermanagementservice.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    boolean existsByUserAndAddress(User user, Address address);
    Optional<UserAddress> findByUserAndDeliveryAddressIsTrue(User user);
    Optional<UserAddress> findByUserAndAddress(User user, Address address);
}
