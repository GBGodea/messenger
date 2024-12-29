package com.godea.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenProvider {
    SecretKey secretKey = Keys.hmacShaKeyFor(JwtConstant.JwtSecret.getBytes());

    public String generateToken(Authentication authentication) {
        return Jwts.builder().issuer("GB_Godea")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .claim("email", authentication.getName())
                .signWith(secretKey) // Подписываю JSON секретным ключом приложения
                .compact();
    }

    public String getEmailFromToken(String jwt) {
        jwt = jwt.substring(7);

        Claims claim = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();
        return String.valueOf(claim.get("email"));
    }
}
