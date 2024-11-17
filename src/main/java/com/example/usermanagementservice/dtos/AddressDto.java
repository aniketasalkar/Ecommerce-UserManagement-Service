package com.example.usermanagementservice.dtos;

import lombok.Data;

@Data
public class AddressDto {

    private String name;

    private String street;

    private String city;

    private String state;

    private String zip;

    private String country;
    private String extaNotes;

    private Boolean deliveryAddress;
}
