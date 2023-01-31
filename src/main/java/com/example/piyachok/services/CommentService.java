package com.example.piyachok.services;

import com.example.piyachok.dao.CommentDAO;
import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.Comment;
import com.example.piyachok.models.Place;
import com.example.piyachok.models.User;
import com.example.piyachok.models.dto.CommentDTO;
import com.example.piyachok.models.dto.ItemListDTO;
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
    private PlaceDAO placeDAO;
    private ItemListService itemListService;

    public static CommentDTO convertCommentToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setText(comment.getText());
        commentDTO.setPlaceId(comment.getPlace().getId());
        commentDTO.setUserLogin(comment.getUser().getLogin());
        commentDTO.setPlaceName(comment.getPlace().getName());
        return commentDTO;
    }

    public ResponseEntity<List<CommentDTO>> findAllComments() {
        List<CommentDTO> commentDTOList = commentDAO.findAll()
                .stream()
                .map(CommentService::convertCommentToCommentDTO)
                .collect(Collectors.toList());
        if (commentDTOList.size() != 0) {
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<CommentDTO>> findCommentsByUserLogin(String login) {
        User user = userDAO.findUserByLogin(login).orElse(new User());
        if (user.getLogin() != null) {
            List<CommentDTO> usersComments = commentDAO.findAllByUser(user)
                    .stream()
                    .map(CommentService::convertCommentToCommentDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(usersComments, HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<CommentDTO> findCommentById(int id) {
        Comment comment = commentDAO.findById(id).orElse(new Comment());
        if (comment.getId() != 0) {
            return new ResponseEntity<>(convertCommentToCommentDTO(comment), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ItemListDTO<CommentDTO>> findCommentsByPlaceId(int placeId, Integer page, Boolean old) {
        int itemsOnPage = 10;
        List<CommentDTO> comments = commentDAO.findAllByPlaceId(placeId)
                .stream()
                .map(CommentService::convertCommentToCommentDTO).collect(Collectors.toList());
        return itemListService.createResponseEntity(comments, itemsOnPage, page, old);

    }

    public ResponseEntity<CommentDTO> saveComment(CommentDTO commentDTO) {
        if (commentDTO != null) {
            Place place = placeDAO.findById(commentDTO.getPlaceId()).orElse(new Place());

            if (place.getName() == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            User user = userDAO.findUserByLogin(commentDTO.getUserLogin()).orElse(new User());

            if (user.getLogin() == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Comment comment = new Comment();
            comment.setText(commentDTO.getText());
            comment.setPlace(place);
            comment.setUser(user);
            commentDAO.save(comment);
            return new ResponseEntity<>(convertCommentToCommentDTO(comment), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<CommentDTO> updateComment(CommentDTO commentDTO) {
        if (commentDTO != null) {
            Comment comment=commentDAO.findById(commentDTO.getId()).orElse(new Comment());

            if (comment.getId()!=0){
                comment.setText(commentDTO.getText());
                commentDAO.save(comment);
                return new ResponseEntity<>(convertCommentToCommentDTO(comment), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }




}
