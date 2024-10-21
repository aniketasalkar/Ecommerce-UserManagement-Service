package com.example.usermanagementservice.controllers;

import com.example.usermanagementservice.dtos.UpdatePasswordRequestDto;
import com.example.usermanagementservice.dtos.UpdatePasswordResponseDto;
import com.example.usermanagementservice.dtos.UserResponseDto;
import com.example.usermanagementservice.exceptions.FieldNotModifiableException;
import com.example.usermanagementservice.exceptions.InvalidDataException;
import com.example.usermanagementservice.models.RequestStatus;
import com.example.usermanagementservice.models.User;
import com.example.usermanagementservice.services.IUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserManagementController {

    @Autowired
    private IUserManagementService userManagementService;

    @PostMapping("/users/{email}/updatePassword")
    public ResponseEntity<UpdatePasswordResponseDto> updatePassword(@PathVariable String email , @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto) {
        try {
            RequestStatus response = userManagementService.updatePassword(email, updatePasswordRequestDto);

            UpdatePasswordResponseDto updatePasswordResponseDto = new UpdatePasswordResponseDto();
            updatePasswordResponseDto.setRequestStatus(response);

            return new ResponseEntity<>(updatePasswordResponseDto, HttpStatus.OK);
        }  catch (Exception exception) {
            throw  exception;
        }
    }

    @PatchMapping("/users/{email}/updateDetails")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String email, @RequestBody Map<String, Object> updateDetails) {

        try {
            validatePatchRequest(updateDetails);
            User user = userManagementService.updateUserInformation(email, updateDetails);

            return new ResponseEntity<>(toDto(user), HttpStatus.OK);
        } catch (Exception exception) {
            throw  exception;
        }

    }

    private void validatePatchRequest(Map<String, Object> patchRequest) {
        for (Map.Entry<String, Object> entry : patchRequest.entrySet()) {
            String key = entry.getKey();

            switch (key) {
                case "firstName":
                    if (!(entry.getValue() instanceof String)) {
                        throw new InvalidDataException("First name must be a string");
                    }
                    if (patchRequest.get("firstName").toString().trim().isEmpty() || patchRequest.get("firstName").toString() == null) {
                        throw new InvalidDataException("firstName cannot be empty");
                    }
                    break;

                case "lastName":
                    if (!(entry.getValue() instanceof String)) {
                        throw new InvalidDataException("Last name must be a string");
                    }
                    if (patchRequest.get("lastName").toString().trim().isEmpty() || patchRequest.get("lastName").toString() == null) {
                        throw new InvalidDataException("lastName cannot be empty");
                    }
                    break;

                case "email":
                    if (!(entry.getValue() instanceof String)) {
                        throw new InvalidDataException("email name must be a string");
                    }
                    if (!patchRequest.get("email").toString().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                        throw new InvalidDataException("Invalid email");
                    }
                    break;

                case "password":
                    throw new FieldNotModifiableException("Password cannot be modified here");

                default:
                    throw new FieldNotModifiableException("Field not modifiable or Invalid field");
            }

            if (entry.getValue() instanceof String) {
                patchRequest.put(key, entry.getValue().toString().strip());
            }
        }
    }

    private UserResponseDto toDto (User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        userResponseDto.setRequestStatus(RequestStatus.SUCCESS);

        return userResponseDto;
    }
}
