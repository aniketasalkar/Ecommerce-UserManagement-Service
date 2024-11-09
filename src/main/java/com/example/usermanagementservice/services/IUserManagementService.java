package com.example.usermanagementservice.services;

import com.example.usermanagementservice.dtos.UpdatePasswordRequestDto;
import com.example.usermanagementservice.models.RequestStatus;
import com.example.usermanagementservice.models.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUserManagementService {
    RequestStatus updatePassword(String email, UpdatePasswordRequestDto updatePasswordRequestDto);
    User updateUserInformation(String email, Map<String, Object> userInformation);
    User getUserDetails(String email);
    List<User> getAllUsers();
    User createUser(User user);
}
