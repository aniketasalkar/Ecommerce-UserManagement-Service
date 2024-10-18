package com.example.usermanagementservice.utils;

import com.example.usermanagementservice.models.User;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Autowired
    SecretKey secretKey;

    private Long accessTokenValidity = 6 * 60 * 60 * 1000L;
    private Long refreshTokenValidity = 7 * 24 * 60 * 60 * 1000L;

    public String generateAccessToken(User user) {
        Map<String, Object> claims = compactClaims(user, accessTokenValidity);
        claims.put("token_type", "AccessToken");

        String accessToken = Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();

        return accessToken;
    }

    public String generateRefreshToken(User user) {

        Map<String, Object> claims = compactClaims(user, refreshTokenValidity);
        claims.put("token_type", "RefreshToken");

        String refreshToken = Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();

        return refreshToken;
    }

//    public Boolean validateToken(String token, User user) {
//
//    }

    private Map<String, Object> compactClaims(User user, Long validityInMilliSeconds) {
        Long currentTimeMillis = System.currentTimeMillis();

        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", "USER");
        claims.put("iat", currentTimeMillis);
        claims.put("expiry", currentTimeMillis + validityInMilliSeconds);
        claims.put("issuer", "shop.at.ecommerce");

        return claims;
    }
}
