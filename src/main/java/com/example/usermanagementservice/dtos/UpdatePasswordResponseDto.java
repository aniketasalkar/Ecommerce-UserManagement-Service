package com.example.usermanagementservice.dtos;

import com.example.usermanagementservice.models.RequestStatus;
import lombok.Data;

@Data
public class UpdatePasswordResponseDto {
    private RequestStatus requestStatus;
}
