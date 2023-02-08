package com.example.piyachok.services;

import com.example.piyachok.dao.RefreshTokenDAO;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.customExceptions.RefreshTokenException;
import com.example.piyachok.models.RefreshToken;
import com.example.piyachok.models.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
        User user = userDAO.findUserByLogin(login).orElse(new User());
        if (user.getRefreshToken()!=null){
            refreshToken=user.getRefreshToken();
        }
        refreshToken.setUser(user);
        refreshToken.setExpirationDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        user.setRefreshToken(refreshToken);
        refreshTokenDAO.save(refreshToken);
        return refreshToken;
    }

    public void deleteAllRefreshTokensByUser(User user) {
        List<RefreshToken> refreshTokens = refreshTokenDAO.findAllByUser(user).orElse(new ArrayList<>());
        if (refreshTokens.size() != 0) {
            refreshTokenDAO.deleteAll(refreshTokens);
        }
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

    public ResponseEntity<List<RefreshToken>> getAllRefreshTokens() {
        List<RefreshToken> refreshTokens = refreshTokenDAO.findAll();
        return new ResponseEntity<>(refreshTokens, HttpStatus.OK);
    }

}
