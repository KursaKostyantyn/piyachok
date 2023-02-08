package com.example.piyachok.dao;

import com.example.piyachok.models.Place;
import com.example.piyachok.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User,Integer> {
    Optional<User> findUserByLogin(String login);
    Optional<List<User>> findAllByFavoritePlaces (Place place);
}
