package com.example.usermanagementservice.utils;

import com.example.usermanagementservice.exceptions.InvalidTokenException;
import com.example.usermanagementservice.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Autowired
    SecretKey secretKey;

    public String generateAccessToken(User user) {
        Long accessTokenValidity = 6 * 60 * 60 * 1000L;

        Map<String, Object> claims = compactClaims(user, accessTokenValidity);
        claims.put("token_type", "AccessToken");

        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(User user) {

        Long refreshTokenValidity = 7 * 24 * 60 * 60 * 1000L;

        Map<String, Object> claims = compactClaims(user, refreshTokenValidity);
        claims.put("token_type", "RefreshToken");

        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }

    public Boolean validateToken(String token_type, String token, User user) {
        Claims claims = getClaimsFromToken(token);
        if (!claims.get("user_id").equals(user.getId())) {
            throw new InvalidTokenException("Invalid token user_id");
        }
        if (!claims.get("email").equals(user.getEmail())) {
            throw new InvalidTokenException("Invalid token email");
        }
        if (!claims.get("token_type").equals(token_type)) {
            throw new InvalidTokenException("Invalid token token_type");
        }
        if (!claims.get("issuer").equals("shop.at.ecommerce")) {
            throw new InvalidTokenException("Invalid token issuer");
        }
        if (!claims.get("roles").equals("USER")) {
            throw new InvalidTokenException("Invalid token roles");
        }

        return !claims.getExpiration().before(new Date());
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
            claims = parser.parseSignedClaims(token).getPayload();
        } catch (Exception exception) {
            throw new InvalidTokenException("Invalid token Signature");
        }

        return claims;
    }

    private Map<String, Object> compactClaims(User user, Long validityInMilliSeconds) {
        Long currentTimeMillis = System.currentTimeMillis();

        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roles", "USER");
        claims.put("iat", currentTimeMillis);
        claims.put("exp", currentTimeMillis + validityInMilliSeconds);
        claims.put("issuer", "shop.at.ecommerce");

        return claims;
    }
}
