package com.example.piyachok.dao;

import com.example.piyachok.models.FavoritePlaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritePlacesDAO extends JpaRepository<FavoritePlaces, Integer> {

    List<FavoritePlaces> findAllByUserId(int id);

}
