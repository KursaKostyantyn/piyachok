package com.example.piyachok.dao;

import com.example.piyachok.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsDAO extends JpaRepository<News,Integer> {

    List<News> findNewsByUserId(int id);
    Optional<News> findNewsById(int id);
}
