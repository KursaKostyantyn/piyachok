package com.example.piyachok.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    public String generateTokenFromUsername(String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .signWith(SignatureAlgorithm.HS512, "secretKey".getBytes())
                .setExpiration(new Date(System.currentTimeMillis() + 50000))
                .compact();
    }

}
