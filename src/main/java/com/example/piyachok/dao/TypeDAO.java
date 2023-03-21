package com.example.piyachok.dao;

import com.example.piyachok.models.Feature;
import com.example.piyachok.models.Place;
import com.example.piyachok.models.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeDAO extends JpaRepository<Type, Integer> {

    Optional<Type> getTypeByName(String name);


    List<Type> findAllByPlacesContaining(Place place);



}
