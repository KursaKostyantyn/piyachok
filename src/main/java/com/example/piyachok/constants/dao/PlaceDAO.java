package com.example.piyachok.constants.dao;

import com.example.piyachok.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceDAO extends JpaRepository<Place, Integer> {
}
