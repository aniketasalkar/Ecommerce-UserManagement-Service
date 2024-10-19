package com.example.usermanagementservice.services;

import com.example.usermanagementservice.dtos.ValidateAndRefreshTokenRequestDto;
import com.example.usermanagementservice.models.RequestStatus;
import com.example.usermanagementservice.models.TokenState;
import com.example.usermanagementservice.models.User;
import org.antlr.v4.runtime.misc.Pair;

public interface IAuthService {
    User signup(User user);
    String login(String email, String password);
    Pair<TokenState, String> validateAndRefreshToken(String email, ValidateAndRefreshTokenRequestDto validateAndRefreshTokenRequestDto);
}
