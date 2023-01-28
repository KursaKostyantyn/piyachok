package com.example.piyachok.dao;

import com.example.piyachok.models.Place;
import com.example.piyachok.models.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeDAO extends JpaRepository<Type,Integer> {

    public Type getTypeByName(String name);

    public List<Type> findAllByPlace(Place place);

}
