package com.example.piyachok.services;

import com.example.piyachok.dao.RefreshTokenDAO;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.customExceptions.RefreshTokenException;
import com.example.piyachok.models.RefreshToken;
import com.example.piyachok.models.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private RefreshTokenDAO refreshTokenDAO;
    private UserDAO userDAO;


    public RefreshToken findRefreshTokenByToken(String token) {
        return refreshTokenDAO.findRefreshTokenByToken(token);
    }

    public RefreshToken createRefreshToken(String login) {
        long refreshTokenDurationMs = 5000000;
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userDAO.findUserByLogin(login));
        refreshToken.setExpirationDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenDAO.save(refreshToken);

        return refreshToken;
    }

    public boolean verifyExpiration(RefreshToken token) {
        if (token.getExpirationDate().compareTo(Instant.now()) < 0) {
            refreshTokenDAO.delete(token);
            throw new RefreshTokenException(token.getToken(), "Refresh token was expired. Please make a new signing request");
        }
        return true;
    }

    public void deleteByUserId(int id) {
        User user = userDAO.findById(id).orElse(new User());
        if (user.getLogin() != null) {
            refreshTokenDAO.deleteByUser(user);
        }
    }

}
