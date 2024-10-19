package com.example.usermanagementservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdatePasswordRequestDto {
    @NotEmpty(message = "Old Password Required")
    private String oldPassword;

    @NotEmpty(message = "New Password Required")
    private String newPassword;

    @NotEmpty(message = "Confirm new Password")
    private String confirmPassword;
}
