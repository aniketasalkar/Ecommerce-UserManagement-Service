package com.example.usermanagementservice.dtos;

import com.example.usermanagementservice.models.RequestStatus;
import com.example.usermanagementservice.models.TokenState;
import lombok.Data;

@Data
public class ValidateAndRefreshTokenResponseDto {
    private String accessToken;
    private String refreshToken;
    private RequestStatus requestStatus;
    private TokenState tokenState;
}
