package com.example.usermanagementservice.controllers;

import com.example.usermanagementservice.dtos.AddressDto;
import com.example.usermanagementservice.dtos.AddressRequestDto;
import com.example.usermanagementservice.dtos.AddressResponseDto;
import com.example.usermanagementservice.dtos.UpdateAddressDto;
import com.example.usermanagementservice.models.Address;
import com.example.usermanagementservice.services.AddressService;
import com.example.usermanagementservice.utils.IDtoMapper;
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

    @Autowired
    IDtoMapper dtoMapper;

    @PostMapping("/{userId}")
    public ResponseEntity<AddressResponseDto> createAddress(@PathVariable Long userId, @RequestBody AddressRequestDto addressRequestDto) {
        try {
            List<Address> addresses = addressService.addAddress(dtoMapper.toAddress(addressRequestDto), userId);

            AddressResponseDto addressResponseDto = new AddressResponseDto();
            addressResponseDto.setUserId(userId);
            addressResponseDto.setAddresses(dtoMapper.toAddressDtoList(addresses, userId));

            return new ResponseEntity<>(addressResponseDto, HttpStatus.CREATED);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @GetMapping("/default_address/{userId}")
    public ResponseEntity<AddressDto> getDefaultAddress(@PathVariable Long userId) {
        try {
            Address address = addressService.getDefaultAddress(userId);
            AddressDto addressDto = new AddressDto();

            return new ResponseEntity<>(dtoMapper.toAddressDto(address), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @PatchMapping("/update_default_address/{userId}")
    public ResponseEntity<AddressDto> updateDefaultAddress(@PathVariable Long userId, @RequestBody UpdateAddressDto updateAddressDto) {
        try {
            Address address = addressService.updateDefaultAddress(updateAddressDto.getAddressId(), userId);
            AddressDto addressDto = new AddressDto();

            return new ResponseEntity<>(dtoMapper.toAddressDto(address), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<AddressResponseDto> getAllAddresses( @PathVariable Long userId) {
        try {
            List<Address> addresses = addressService.getAddressesByUserId(userId);

            AddressResponseDto addressResponseDto = new AddressResponseDto();
            addressResponseDto.setUserId(userId);
            addressResponseDto.setAddresses(dtoMapper.toAddressDtoList(addresses, userId));

            return new ResponseEntity<>(addressResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }
}
