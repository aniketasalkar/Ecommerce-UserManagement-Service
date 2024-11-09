package com.example.usermanagementservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {
    @Email
    @NotEmpty(message = "Email cannot be null")
    private String email;

//    @Size(min = 8, max = 16)
////    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
////            message = "Password must contain at least one uppercase letter, one numeric value, and one special character.")
//    private String password;

    @NotEmpty(message = "FirstName cannot be empty")
    private String firstName;

    @NotEmpty(message = "LastName cannot be empty")
    private String lastName;

    @Size(min = 10, max = 10, message = "phone number cannot be empty")
//    @Pattern(regexp = "^\\+?[0-9]{1,4}?[-.\\s]?\\(?[0-9]{1,3}?\\)?[-.\\s]?[0-9]{3,4}[-.\\s]?[0-9]{4}$",
//            message = "Invalid phone number format.")
    private String phoneNumber;

//    private List<Address> addresses;
//    private List<UserRoles> roles;
}
