package org.example.ovr.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.ovr.Config.JwtProperties;
import org.example.ovr.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
/*
@Component
public class JwtProvider {

    @Autowired
    private JwtProperties jwtProperties;

    // Generate token from email and role
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }

    // Overload: generate token from User object
    public String generateToken(User user) {
        return generateToken(user.getEmail(), user.getRole());
    }

    // Parse claims
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Extract email from token
    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    // Extract role from token
    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }
}*/

@Component
public class JwtProvider {

    @Autowired
    private JwtProperties jwtProperties;

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole()) // e.g. ROLE_ADMIN
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }
}