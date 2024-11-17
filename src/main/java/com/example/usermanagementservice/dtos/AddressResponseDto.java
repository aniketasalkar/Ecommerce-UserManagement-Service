package com.example.usermanagementservice.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AddressResponseDto {
    private Long userId;

    private List<AddressDto> addresses;
}
