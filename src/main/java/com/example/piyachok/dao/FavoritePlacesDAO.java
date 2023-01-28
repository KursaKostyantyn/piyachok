package com.example.piyachok.dao;

import com.example.piyachok.models.FavoritePlace;
import com.example.piyachok.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritePlacesDAO extends JpaRepository<FavoritePlace, Integer> {


}
