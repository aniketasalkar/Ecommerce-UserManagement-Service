package com.example.usermanagementservice.repository;

import com.example.usermanagementservice.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Address save(Address address);
    Optional<Address> findByNameAndStreetAndCityAndStateAndZipAndCountry(String name, String street, String city, String state, String zip, String country);

    Optional<Address> findAddressesById(Long id);
//    Optional<Address> findByUsersIdAndDeliveryAddressTrue(Long userid);
//    @Query("SELECT a FROM Address a JOIN a.users u WHERE u.id = :userId AND a.deliveryAddress = true")
//    Optional<Address> findDeliveryAddressForUser(@Param("userId") Long userId);
//    List<Address> findAddressesById_AddressId(Long userId);
}
