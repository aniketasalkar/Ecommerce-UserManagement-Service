package com.example.usermanagementservice.services;

import com.example.usermanagementservice.dtos.ValidateAndRefreshTokenRequestDto;
import com.example.usermanagementservice.exceptions.BadCredentialsException;
import com.example.usermanagementservice.exceptions.InvalidTokenException;
import com.example.usermanagementservice.exceptions.UserAlreadyExistsException;
import com.example.usermanagementservice.exceptions.UserNotFoundException;
import com.example.usermanagementservice.models.*;
import com.example.usermanagementservice.repository.SessionRepository;
import com.example.usermanagementservice.repository.UserRepository;
import com.example.usermanagementservice.utils.JwtUtils;
import org.antlr.v4.runtime.misc.Pair;
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

    @Override
    public Pair<TokenState, String> validateAndRefreshToken(String email, ValidateAndRefreshTokenRequestDto validateAndRefreshTokenRequestDto) {
        Pair<TokenState, String> response = new Pair<>(TokenState.ACTIVE, validateAndRefreshTokenRequestDto.getAccessToken());

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        Session session = sessionRepository.findSessionByAccessTokenAndRefreshToken(
                validateAndRefreshTokenRequestDto.getAccessToken(),
                validateAndRefreshTokenRequestDto.getRefreshToken()
//                SessionState.ACTIVE
                ).orElseThrow(() -> new InvalidTokenException("Invalid token"));

        if (jwtUtils.validateToken("RefreshToken", session.getRefreshToken(), user)) {
            if (!jwtUtils.validateToken("AccessToken", session.getAccessToken(), user)) {
                String newAccessToken = jwtUtils.generateAccessToken(user);
                session.setAccessToken(newAccessToken);
                session.setUpdatedAt(new Date());

                sessionRepository.save(session);
                response = new Pair<>(TokenState.REFRESHED, newAccessToken);
            }
        } else {
            session.setSessionState(SessionState.INACTIVE);
            session.setUpdatedAt(new Date());

            sessionRepository.save(session);
            response = new Pair<>(TokenState.EXPIRED, "");
        }

        return response;
    }
}
