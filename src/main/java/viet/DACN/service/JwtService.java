package viet.DACN.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import viet.DACN.util.TokenType;

public interface JwtService {
    String generateToken(UserDetails user);

    String generateRefreshToken(UserDetails user);

    String generateResetToken(UserDetails user);

    String extractUsername(String token, TokenType type);

    boolean isValid(String token, TokenType type, UserDetails user);

    List<String> extractRoles(String token, TokenType tokenType);
    
}
