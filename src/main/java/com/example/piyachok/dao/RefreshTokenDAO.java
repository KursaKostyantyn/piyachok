package com.example.piyachok.dao;

import com.example.piyachok.models.RefreshToken;
import com.example.piyachok.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenDAO extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findRefreshTokenByToken(String token);

    void deleteByUser(User user);
}
