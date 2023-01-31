package com.example.piyachok.dao;

import com.example.piyachok.models.Comment;
import com.example.piyachok.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDAO extends JpaRepository<Comment, Integer> {
 List<Comment> findAllByUser(User user);
 List<Comment> findAllByPlaceId(int placeId);

}
