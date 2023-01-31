package com.example.piyachok.controllers;

import com.example.piyachok.models.Comment;
import com.example.piyachok.models.dto.CommentDTO;
import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("")
public class CommentController {

    private CommentService commentService;

    @GetMapping("myCabinet/myComments")
    public ResponseEntity<List<CommentDTO>> findCommentsByUserLogin (@RequestParam String login){
        return commentService.findCommentsByUserLogin(login);
    }

    @GetMapping("myCabinet/myComments/{id}")
    public ResponseEntity<CommentDTO> findCommentById(@PathVariable int id){
        return commentService.findCommentById(id);
    }

    @GetMapping("comments/placeComments")
    public ResponseEntity<ItemListDTO<CommentDTO>> findCommentsByPlaceId(@RequestParam int placeId,@RequestParam(required = false) Integer page,@RequestParam(required = false) Boolean old){
        return commentService.findCommentsByPlaceId(placeId, page, old);
    }


    @PostMapping("comments")
    public ResponseEntity<CommentDTO> saveComment(@RequestBody CommentDTO commentDTO){
        return commentService.saveComment(commentDTO);
    }

    @PutMapping("comments")
    public ResponseEntity<CommentDTO> updateComment(@RequestBody CommentDTO commentDTO){
        return commentService.updateComment(commentDTO);
    }


}
