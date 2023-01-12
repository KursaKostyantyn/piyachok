package com.example.piyachok.services;

import com.example.piyachok.dao.CommentDAO;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.Comment;
import com.example.piyachok.models.User;
import com.example.piyachok.models.dto.CommentDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CommentService {

    private CommentDAO commentDAO;
    private UserDAO userDAO;

    public CommentDTO convertCommentToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setText(comment.getText());
        commentDTO.setPlace(comment.getPlace());
        commentDTO.setUser(comment.getUser());
        return commentDTO;
    }

    public ResponseEntity<List<CommentDTO>> findAllComments() {
        List<CommentDTO> commentDTOList = commentDAO.findAll()
                .stream()
                .map(this::convertCommentToCommentDTO)
                .collect(Collectors.toList());
        if (commentDTOList.size() != 0) {
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<CommentDTO>> findCommentsByUserLogin(String login) {
        User user = userDAO.findUserByLogin(login);
        if (user.getLogin() != null) {
            List<CommentDTO> usersComments = commentDAO.findAllByUser(user)
                    .stream()
                    .map(this::convertCommentToCommentDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(usersComments, HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
