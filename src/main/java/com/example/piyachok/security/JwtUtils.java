package com.example.piyachok.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {


    public String generateTokenFromUsername(String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .signWith(SignatureAlgorithm.HS512, "secretKey".getBytes())
                .setExpiration(new Date(System.currentTimeMillis() + 1000000))
                .compact();
    }


}
