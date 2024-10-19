package com.example.usermanagementservice.controllers;

import com.example.usermanagementservice.dtos.*;
import com.example.usermanagementservice.models.RequestStatus;
import com.example.usermanagementservice.models.TokenState;
import com.example.usermanagementservice.models.User;
import com.example.usermanagementservice.services.IAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
//        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
//        headers.add(HttpHeaders.);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setRequestStatus(RequestStatus.SUCCESS);

        return new ResponseEntity<>(loginResponseDto, headers, HttpStatus.OK);
    }


//    To Do
    @PostMapping("/{email}/validateAndRefreshToken")
    public ResponseEntity<ValidateAndRefreshTokenResponseDto> validateAndRefreshToken(@PathVariable String email, @RequestBody @Valid ValidateAndRefreshTokenRequestDto validateAndRefreshTokenRequestDto) {
        try {
            Pair<TokenState, String> tokenValidity = authService.validateAndRefreshToken(email, validateAndRefreshTokenRequestDto);

            ValidateAndRefreshTokenResponseDto validateAndRefreshTokenResponseDto = new ValidateAndRefreshTokenResponseDto();
            validateAndRefreshTokenResponseDto.setTokenState(tokenValidity.a);
            validateAndRefreshTokenResponseDto.setAccessToken(tokenValidity.b);
            validateAndRefreshTokenResponseDto.setRefreshToken(validateAndRefreshTokenRequestDto.getRefreshToken());
            validateAndRefreshTokenResponseDto.setRequestStatus(RequestStatus.SUCCESS);

            return new ResponseEntity<>(validateAndRefreshTokenResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }

    }

    @DeleteMapping("/{email}/logout")
    public ResponseEntity<LogOutResponseDto> logoutUser (@RequestHeader("Set-Cookie") String authHeader,
                                                         @PathVariable String email,
                                                         HttpServletResponse httpServletResponse) {
        try {
            Boolean isLoggedOut = authService.logout(email, authHeader);

            if (isLoggedOut) {
                httpServletResponse.setHeader("Set-Cookie", "");
                httpServletResponse.setHeader("Set-Cookie2", "");
//                clearCookies(httpServletResponse);
            }
            LogOutResponseDto logOutResponseDto = new LogOutResponseDto();
            logOutResponseDto.setRequestStatus(RequestStatus.SUCCESS);

            return new ResponseEntity<>(logOutResponseDto, HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

//    private void clearCookies(HttpServletResponse response) {
//        Cookie cookie = new Cookie("access_token", null);
//        cookie.setPath("/"); // Set the path to match the cookie path
//        cookie.setHttpOnly(true); // Make it HTTP only
//        cookie.setMaxAge(0); // Set cookie age to 0 to delete it
//        response.addCookie(cookie);
//
//        // Repeat for refresh token or any other cookies you want to clear
//        Cookie refreshCookie = new Cookie("refresh_token", null);
//        refreshCookie.setPath("/");
//        refreshCookie.setHttpOnly(true);
//        refreshCookie.setMaxAge(0);
//        response.addCookie(refreshCookie);
//    }

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
