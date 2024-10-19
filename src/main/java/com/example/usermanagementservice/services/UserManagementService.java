package com.example.usermanagementservice.services;

import com.example.usermanagementservice.dtos.UpdatePasswordRequestDto;
import com.example.usermanagementservice.exceptions.PasswordDoesNotMatchException;
import com.example.usermanagementservice.exceptions.UpdatePasswordException;
import com.example.usermanagementservice.exceptions.UserNotFoundException;
import com.example.usermanagementservice.models.RequestStatus;
import com.example.usermanagementservice.models.User;
import com.example.usermanagementservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserManagementService implements IUserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public RequestStatus updatePassword(String email, UpdatePasswordRequestDto updatePasswordRequestDto) {

        if (!userRepository.findByEmail(email).isPresent()) {
            throw new UserNotFoundException("User Not Found");
        }
        User user = userRepository.findByEmail(email).get();

        if (!bCryptPasswordEncoder.matches(updatePasswordRequestDto.getOldPassword(), user.getPassword())) {
            throw new PasswordDoesNotMatchException("Old Password Not Matched");
        }

        if (bCryptPasswordEncoder.matches(updatePasswordRequestDto.getNewPassword(), user.getPassword())) {
            throw new UpdatePasswordException("New password should not be same as old password");
        }

        if (!updatePasswordRequestDto.getNewPassword().equals(updatePasswordRequestDto.getConfirmPassword())) {
            throw new PasswordDoesNotMatchException("Confirm Password Not Matched with new password");
        }

        user.setPassword(bCryptPasswordEncoder.encode(updatePasswordRequestDto.getNewPassword()));
        user.setUpdatedAt(new Date());
        userRepository.save(user);

        return RequestStatus.SUCCESS;
    }
}
