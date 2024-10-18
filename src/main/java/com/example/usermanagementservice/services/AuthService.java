package com.example.usermanagementservice.services;

import com.example.usermanagementservice.exceptions.BadCredentialsException;
import com.example.usermanagementservice.exceptions.UserAlreadyExistsException;
import com.example.usermanagementservice.exceptions.UserNotFoundException;
import com.example.usermanagementservice.models.Session;
import com.example.usermanagementservice.models.SessionState;
import com.example.usermanagementservice.models.User;
import com.example.usermanagementservice.repository.SessionRepository;
import com.example.usermanagementservice.repository.UserRepository;
import com.example.usermanagementservice.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService implements IAuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    SecretKey secretKey;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public User signup(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Date now = new Date();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        Date now = new Date();

        Session session = new Session();
        session.setUser(user);
        session.setRefreshToken(refreshToken);
        session.setAccessToken(accessToken);
        session.setCreatedAt(now);
        session.setUpdatedAt(now);
        session.setSessionState(SessionState.ACTIVE);

        if (sessionRepository.findByUserIdAndSessionState(user.getId(), SessionState.ACTIVE).isPresent()) {
            Session prevSession = sessionRepository.findByUserIdAndSessionState(user.getId(), SessionState.ACTIVE).get();
            prevSession.setSessionState(SessionState.INACTIVE);
            prevSession.setUpdatedAt(new Date());
            sessionRepository.save(prevSession);
        }

        sessionRepository.save(session);


        return accessToken + ":" + refreshToken;
    }
}
