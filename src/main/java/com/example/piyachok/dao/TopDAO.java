package com.example.piyachok.dao;

import com.example.piyachok.models.Place;
import com.example.piyachok.models.Top;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopDAO extends JpaRepository<Top,Integer> {
 Optional<List<Top>> findAllByPlacesContaining(Place place);
}
