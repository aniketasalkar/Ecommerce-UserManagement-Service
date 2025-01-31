package com.example.usermanagementservice.services;

import com.example.usermanagementservice.models.Address;

import java.util.List;

public interface IAddressService {
    List<Address> addAddress(Address address, Long userId);
    Address getDefaultAddress(Long userId);
    Address updateDefaultAddress(Long addressId, Long userId);
    List<Address> getAddressesByUserId(Long userId);
}
