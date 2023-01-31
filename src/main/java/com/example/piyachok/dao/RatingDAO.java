package com.example.piyachok.dao;

import com.example.piyachok.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingDAO extends JpaRepository<Rating, Integer> {

    List<Rating> findAllByUser_Login(String login);
    List<Rating> findAllByPlaceId(int id);
    List<Rating> findAllByPlaceIdAndUserLogin(int placeId, String userLogin);
    Optional<Rating> findRatingByPlaceIdAndUserLogin(int placeId, String userLogin);


}
