package com.example.usermanagementservice.utils;

import com.example.usermanagementservice.dtos.AddressDto;
import com.example.usermanagementservice.dtos.AddressRequestDto;
import com.example.usermanagementservice.models.Address;
import com.example.usermanagementservice.models.UserAddress;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DtoMapper implements IDtoMapper {

    @Override
    public Address toAddress(AddressRequestDto addressRequestDto) {
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

    @Override
    public List<AddressDto> toAddressDtoList(List<Address> addresses, Long userId) {
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
//            addressDto.setDeliveryAddress(address.getUserAddresses().stream().filter(userAddress -> {
//                    if (userAddress.getAddress().getState().equals(address.getState()) &&
//                            userAddress.getAddress().getCountry().equals(address.getCountry()) &&
//                            userAddress.getAddress().getZip().equals(address.getZip()) &&
//                            userAddress.getAddress().getStreet().equals(address.getStreet()) &&
//                            userAddress.getAddress().getExtaNotes().equals(address.getExtaNotes()) &&
//                            userAddress.getAddress().getCity().equals(address.getCity()) &&
//                            userAddress.getAddress().getName().equals(address.getName())
//                            ) {
//                        return true;
//                    }
//                    return false;
//                }).map(userAddress -> userAddress.getDeliveryAddress()).findFirst().get());
            addressDto.setDeliveryAddress(
                    address.getUserAddresses().stream()
                            .anyMatch(UserAddress::getDeliveryAddress) // True if ANY user marks it as delivery
            );

            addressResponseDto.add(addressDto);
        }

        return addressResponseDto;
    }

    @Override
    public AddressDto toAddressDto(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setName(address.getName());
        addressDto.setCity(address.getCity());
        addressDto.setState(address.getState());
        addressDto.setCountry(address.getCountry());
        addressDto.setZip(address.getZip());
        addressDto.setStreet(address.getStreet());
        addressDto.setExtaNotes(address.getExtaNotes());
        addressDto.setDeliveryAddress(true);

        return addressDto;
    }
}
