package com.example.piyachok.dao;

import com.example.piyachok.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface NewsDAO extends JpaRepository<News,Integer> {
}