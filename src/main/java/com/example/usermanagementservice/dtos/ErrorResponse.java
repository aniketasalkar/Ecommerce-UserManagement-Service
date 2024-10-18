package com.example.usermanagementservice.dtos;

import com.example.usermanagementservice.models.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private RequestStatus requestStatus;
}
