package com.example.usermanagementservice.controllers;

import com.example.usermanagementservice.dtos.UpdatePasswordRequestDto;
import com.example.usermanagementservice.dtos.UpdatePasswordResponseDto;
import com.example.usermanagementservice.models.RequestStatus;
import com.example.usermanagementservice.services.IUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
