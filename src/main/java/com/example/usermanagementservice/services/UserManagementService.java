package com.example.usermanagementservice.services;

import com.example.usermanagementservice.clients.KafkaProducerClient;
import com.example.usermanagementservice.dtos.EmailDto;
import com.example.usermanagementservice.dtos.UpdatePasswordRequestDto;
import com.example.usermanagementservice.exceptions.*;
import com.example.usermanagementservice.models.RequestStatus;
import com.example.usermanagementservice.models.User;
import com.example.usermanagementservice.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserManagementService implements IUserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducerClient kafkaProducerClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${welcomeEmail.sender}")
    private String senderEmail;

    private static final Logger logger = LoggerFactory.getLogger(UserManagementService.class);

    @Override
    public RequestStatus updatePassword(String email, UpdatePasswordRequestDto updatePasswordRequestDto) {

//        if (!userRepository.findByEmail(email).isPresent()) {
//            throw new UserNotFoundException("User Not Found");
//        }
//        User user = userRepository.findByEmail(email).get();
//
//        if (!bCryptPasswordEncoder.matches(updatePasswordRequestDto.getOldPassword(), user.getPassword())) {
//            throw new PasswordDoesNotMatchException("Old Password Not Matched");
//        }
//
//        if (bCryptPasswordEncoder.matches(updatePasswordRequestDto.getNewPassword(), user.getPassword())) {
//            throw new UpdatePasswordException("New password should not be same as old password");
//        }
//
//        if (!updatePasswordRequestDto.getNewPassword().equals(updatePasswordRequestDto.getConfirmPassword())) {
//            throw new PasswordDoesNotMatchException("Confirm Password Not Matched with new password");
//        }
//
////        user.setPassword(bCryptPasswordEncoder.encode(updatePasswordRequestDto.getNewPassword()));
//        user.setUpdatedAt(new Date());
//        userRepository.save(user);

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

    @Override
    @Transactional
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        Date now = new Date();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return userRepository.save(user);
    }

    @Override
    public Boolean sendWelcomeEmail(String email) {
        String topic = "welcome-email";
        String subject = "Welcome Email";
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        String body = """
            Hi %s,
            
            Welcome to Shop.Ecommerce! 🎉

            We're thrilled to have you join our community of shoppers! Now that you're all set up, you're ready to discover thousands of amazing products, exclusive deals, and much more.

            Here's a quick overview of what you can do next:
            
            What’s Waiting for You?
            - 🛒 Shop the Latest Trends: Browse through our wide range of categories – from fashion and electronics to home goods and beauty products.
            - 🚚 Fast & Secure Shipping: We offer fast and reliable shipping right to your doorstep.
            - 🎁 Special Offers Just for You: As a new member, you’ll get exclusive deals, discounts, and early access to sales. Don’t miss out!
            - 🔒 Safe & Secure Shopping: Your privacy and security are our top priorities. Shop with confidence knowing your personal information is safe with us.

            Need Help?
            If you have any questions or need assistance, our customer support team is here to help! You can reach us at support@shop.ecommerce or visit our Help Center on the website.

            Get Started Now
            Ready to shop? Click here to browse our collection and start shopping!

            Thank you for choosing Shop.Ecommerce! We can’t wait to help you find exactly what you’re looking for.

            Warm regards,  
            The Shop.Ecommerce Team
            shop.ecommerce | Contact Us | Privacy Policy

            P.S. Keep an eye on your inbox for upcoming promotions and sales! 🎉
            """;
        body = String.format(body, user.getFirstName());
        EmailDto emailDto = new EmailDto();
        emailDto.setFromEmail(senderEmail);
        emailDto.setToEmail(email);
        emailDto.setSubject(subject);
        emailDto.setBody(body);
        try {
            kafkaProducerClient.sendMessage(topic, objectMapper.writeValueAsString(emailDto));
            logger.info("Sent email to topic: " + topic);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

        return true;
    }

    @Override
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        userRepository.deleteByEmail(user.getEmail());
    }

    @Override
    public User getUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found"));

        return user;
    }
}
