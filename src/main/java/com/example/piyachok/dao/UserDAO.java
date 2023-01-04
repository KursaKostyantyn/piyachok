package com.example.piyachok.dao;

import com.example.piyachok.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User,Integer> {
    User findUserByLogin(String login);
}
