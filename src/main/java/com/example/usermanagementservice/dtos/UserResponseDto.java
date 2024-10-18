package com.example.usermanagementservice.dtos;

import com.example.usermanagementservice.models.RequestStatus;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private RequestStatus requestStatus;
}
