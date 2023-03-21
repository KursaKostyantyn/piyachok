package com.example.piyachok.security;

import com.example.piyachok.customExceptions.JwtTokenException;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private final long expirationTimeForAccessToken = 10000000;
    private final long expirationTimeForRefreshToken = 100000000;
    private final long expirationTimeForActivationUserToken = 1000000000;

    private String generateTokenFromUsernameAndSetExpirationTime(String userName, long expirationTime) {
        return Jwts.builder()
                .setSubject(userName)
                .signWith(SignatureAlgorithm.HS512, "secretKey".getBytes())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .compact();
    }

    public String generateTokenFromUsernameForAccessToken(String userName) {
        return generateTokenFromUsernameAndSetExpirationTime(userName, expirationTimeForAccessToken);
    }

    public String generateTokenFromUsernameForRefreshToken(String userName) {
        return generateTokenFromUsernameAndSetExpirationTime(userName, expirationTimeForRefreshToken);
    }

    public String generateTokenFromUsernameForActivationUser(String userName) {
        return generateTokenFromUsernameAndSetExpirationTime(userName, expirationTimeForActivationUserToken);
    }

    public String getUserLoginFromJwtToken(String jwtToken) {
        try {
            return Jwts.parser()
                    .setSigningKey("secretKey".getBytes())
                    .parseClaimsJws(jwtToken)
                    .getBody()
                    .getSubject();

        } catch (ExpiredJwtException expiredJwtException) {
            throw new JwtTokenException(jwtToken, "jwtToken is expired");
        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new JwtTokenException(jwtToken, "unsupported Jwt Exception");
        } catch (MalformedJwtException malformedJwtException) {
            throw new JwtTokenException(jwtToken, "malformed Jwt Exception");
        } catch (SignatureException signatureException) {
            throw new JwtTokenException(jwtToken, "signatureException");
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new JwtTokenException(jwtToken, "illegal Argument Exception");
        }
    }

}
