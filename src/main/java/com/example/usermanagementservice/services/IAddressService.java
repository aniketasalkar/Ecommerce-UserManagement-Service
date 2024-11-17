package com.example.usermanagementservice.services;

import com.example.usermanagementservice.models.Address;

import java.util.List;

public interface IAddressService {
    List<Address> addAddress(Address address, Long userId);
}
