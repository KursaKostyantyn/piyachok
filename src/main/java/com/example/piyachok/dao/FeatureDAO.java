package com.example.piyachok.dao;

import com.example.piyachok.models.Feature;
import com.example.piyachok.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeatureDAO extends JpaRepository<Feature, Integer> {
    Optional<Feature> findFeatureById(int id);
    List<Feature> findAllByPlacesContaining(Place place);
    Optional<Feature> findByName(String name);
}
