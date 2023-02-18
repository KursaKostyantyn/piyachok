package com.example.piyachok.dao;

import com.example.piyachok.models.Place;
import com.example.piyachok.models.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceDAO extends JpaRepository<Place, Integer> {

    Optional<List<Place>> findAllByUser_Login(String userLogin);

    List<Place> findAllByTypesContains(Type type);

    List<Place> findAllByActivatedTrue();

    List<Place> findAllByActivatedFalse();

}
