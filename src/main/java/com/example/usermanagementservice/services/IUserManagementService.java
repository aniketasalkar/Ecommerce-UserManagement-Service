package com.example.usermanagementservice.services;

import com.example.usermanagementservice.dtos.UpdatePasswordRequestDto;
import com.example.usermanagementservice.models.RequestStatus;

public interface IUserManagementService {
    RequestStatus updatePassword(String email, UpdatePasswordRequestDto updatePasswordRequestDto);
}
