package com.example.usermanagementservice.controllers;

import com.example.usermanagementservice.dtos.AddressDto;
import com.example.usermanagementservice.dtos.AddressRequestDto;
import com.example.usermanagementservice.dtos.AddressResponseDto;
import com.example.usermanagementservice.models.Address;
import com.example.usermanagementservice.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @PostMapping("/{userId}")
    public ResponseEntity<AddressResponseDto> createAddress(@PathVariable Long userId, @RequestBody AddressRequestDto addressRequestDto) {
        try {
            List<Address> addresses = addressService.addAddress(from(addressRequestDto), userId);

            AddressResponseDto addressResponseDto = new AddressResponseDto();
            addressResponseDto.setUserId(userId);
            addressResponseDto.setAddresses(from(addresses));

            return new ResponseEntity<>(addressResponseDto, HttpStatus.CREATED);
        } catch (Exception exception) {
            throw exception;
        }
    }

    private Address from (AddressRequestDto addressRequestDto) {
        Address address = new Address();
        address.setName(addressRequestDto.getName());
        address.setCity(addressRequestDto.getCity());
        address.setState(addressRequestDto.getState());
        address.setCountry(addressRequestDto.getCountry());
        address.setZip(addressRequestDto.getZip());
        address.setStreet(addressRequestDto.getStreet());
        address.setExtaNotes(addressRequestDto.getExtaNotes());

        return address;
    }

    private List<AddressDto> from (List<Address> addresses) {
        List<AddressDto> addressResponseDto = new ArrayList<>();
        for (Address address : addresses) {
            AddressDto addressDto = new AddressDto();
            addressDto.setName(address.getName());
            addressDto.setCity(address.getCity());
            addressDto.setState(address.getState());
            addressDto.setCountry(address.getCountry());
            addressDto.setZip(address.getZip());
            addressDto.setStreet(address.getStreet());
            addressDto.setExtaNotes(address.getExtaNotes());
            addressDto.setDeliveryAddress(address.getDeliveryAddress());

            addressResponseDto.add(addressDto);
        }

        return addressResponseDto;
    }
}
