package com.example.usermanagementservice.controllers;

import com.example.usermanagementservice.dtos.LoginRequestDto;
import com.example.usermanagementservice.dtos.LoginResponseDto;
import com.example.usermanagementservice.dtos.UserRequestDto;
import com.example.usermanagementservice.dtos.UserResponseDto;
import com.example.usermanagementservice.models.RequestStatus;
import com.example.usermanagementservice.models.User;
import com.example.usermanagementservice.services.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserAuthController {

    @Autowired
    IAuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<UserResponseDto> signUpUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        try {
            User user = authService.signup(from(userRequestDto));

            return new ResponseEntity<>(toDto(user), HttpStatus.CREATED);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        String token = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        String[] tokens = token.split(":");
        String accessToken = tokens[0];
        String refreshToken = tokens[1];

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, accessToken);
        headers.add(HttpHeaders.SET_COOKIE2, refreshToken);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setRequestStatus(RequestStatus.SUCCESS);

        return new ResponseEntity<>(loginResponseDto, headers, HttpStatus.OK);
    }

    private User from (UserRequestDto userRequestDto) {
        User user = new User();
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());

        return user;
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
