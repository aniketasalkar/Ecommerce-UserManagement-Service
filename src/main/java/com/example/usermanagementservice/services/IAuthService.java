package com.example.usermanagementservice.services;

import com.example.usermanagementservice.models.User;

public interface IAuthService {
    User signup(User user);
    String login(String email, String password);
}
