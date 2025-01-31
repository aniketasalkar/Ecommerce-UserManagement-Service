package com.example.usermanagementservice.utils;

import com.example.usermanagementservice.dtos.AddressDto;
import com.example.usermanagementservice.dtos.AddressRequestDto;
import com.example.usermanagementservice.models.Address;

import java.util.List;

public interface IDtoMapper {
    Address toAddress(AddressRequestDto addressRequestDto);
    List<AddressDto> toAddressDtoList(List<Address> addresses, Long userId);
    AddressDto toAddressDto(Address address);
}
