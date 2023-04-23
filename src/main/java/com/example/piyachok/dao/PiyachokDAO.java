package com.example.piyachok.dao;

import com.example.piyachok.models.Piaychok;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PiyachokDAO extends JpaRepository<Piaychok,Integer> {
    Optional<List<Piaychok>> findAllByPlaceId(int placeId);

}
