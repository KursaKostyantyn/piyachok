package com.example.piyachok.services;

import com.example.piyachok.constants.Role;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
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

    public ResponseEntity<ItemListDTO<CommentDTO>> findCommentsByUserLogin(String login, Integer page, Boolean old) {
        int itemsOnPage = 10;
        User user = userDAO.findUserByLogin(login).orElse(new User());
        if (user.getLogin() != null) {
            List<CommentDTO> usersComments = commentDAO.findAllByUser(user)
                    .stream()
                    .map(CommentService::convertCommentToCommentDTO)
                    .collect(Collectors.toList());
            return itemListService.createResponseEntity(usersComments, itemsOnPage, page, old);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<CommentDTO> findCommentById(int commentId) {

        Comment comment = commentDAO.findById(commentId).orElse(new Comment());
        if (comment.getId()!=0){
            if (SecurityService.authorizedUserHasRole(Role.ROLE_SUPERADMIN.getUserRole())) {
                return new ResponseEntity<>(convertCommentToCommentDTO(comment), HttpStatus.OK);
            }

            if (SecurityService.getLoginAuthorizedUser().equals(comment.getUser().getLogin())) {
                return new ResponseEntity<>(convertCommentToCommentDTO(comment), HttpStatus.OK);
            }
            if (!SecurityService.getLoginAuthorizedUser().equals(comment.getUser().getLogin())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
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
            Comment comment = commentDAO.findById(commentDTO.getId()).orElse(new Comment());
            if (comment.getId() == 0) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (SecurityService.authorizedUserHasRole(Role.ROLE_SUPERADMIN.getUserRole()) || SecurityService.getLoginAuthorizedUser().equals(comment.getUser().getLogin())) {
                comment.setText(commentDTO.getText());
                commentDAO.save(comment);
                return new ResponseEntity<>(convertCommentToCommentDTO(comment), HttpStatus.OK);
            }
            if (!SecurityService.getLoginAuthorizedUser().equals(comment.getUser().getLogin())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<ItemListDTO<CommentDTO>> findAllComments(Integer page, Boolean old) {
        int itemsOnPage = 10;
        List<CommentDTO> comments = commentDAO.findAll()
                .stream()
                .map(CommentService::convertCommentToCommentDTO)
                .collect(Collectors.toList());
        if (comments.size() != 0) {
            return itemListService.createResponseEntity(comments, itemsOnPage, page, old);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<HttpStatus> deleteCommentById(int id) {
        Comment comment = commentDAO.findById(id).orElse(new Comment());
        if (comment.getId() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (SecurityService.authorizedUserHasRole(Role.ROLE_SUPERADMIN.getUserRole()) ||
                SecurityService.getLoginAuthorizedUser().equals(comment.getUser().getLogin())) {
            commentDAO.delete(comment);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if (!SecurityService.getLoginAuthorizedUser().equals(comment.getUser().getLogin())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
