package com.example.usermanagementservice.services;

import com.example.usermanagementservice.dtos.UpdatePasswordRequestDto;
import com.example.usermanagementservice.exceptions.*;
import com.example.usermanagementservice.models.RequestStatus;
import com.example.usermanagementservice.models.User;
import com.example.usermanagementservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Override
    public User updateUserInformation(String email, Map<String, Object> userInformation) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User Not Found"));

        for (Map.Entry<String, Object> entry : userInformation.entrySet()) {
            Field field = ReflectionUtils.findField(User.class, entry.getKey());
            if (field == null) {
                throw new InvalidFieldException("Invalid Field");
            }
            field.setAccessible(true);

            if (field.getName().equals("email")) {
                Optional<User> emailExists = userRepository.findByEmail(entry.getValue().toString());
                if (emailExists.isPresent()) {
                    throw new UserAlreadyExistsException("User with updated email Already Exists");
                }
            }
            ReflectionUtils.setField(field, user, entry.getValue().toString().strip());
        }

        user.setUpdatedAt(new Date());

        return userRepository.save(user);
    }

    @Override
    public User getUserDetails(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User Not Found"));

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByIdDesc();
    }
}
