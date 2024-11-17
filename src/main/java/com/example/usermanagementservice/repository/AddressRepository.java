package com.example.usermanagementservice.repository;

import com.example.usermanagementservice.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Address save(Address address);
//    List<Address> findAddressesById_AddressId(Long userId);
}
