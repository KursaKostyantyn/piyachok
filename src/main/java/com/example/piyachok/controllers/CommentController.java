package com.example.piyachok.controllers;

import com.example.piyachok.models.Comment;
import com.example.piyachok.models.dto.CommentDTO;
import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("comments")
public class CommentController {

    private CommentService commentService;

    @GetMapping("/myComments")
    public ResponseEntity<ItemListDTO<CommentDTO>> findCommentsByUserLogin (@RequestParam String login, @RequestParam(required = false) Integer page, @RequestParam(required = false) Boolean old){
        return commentService.findCommentsByUserLogin(login,page,old);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> findCommentById(@PathVariable int commentId){
        return commentService.findCommentById(commentId);
    }

    @GetMapping("/placeComments")
    public ResponseEntity<ItemListDTO<CommentDTO>> findCommentsByPlaceId(@RequestParam int placeId,@RequestParam(required = false) Integer page,@RequestParam(required = false) Boolean old){
        return commentService.findCommentsByPlaceId(placeId, page, old);
    }


    @PostMapping("")
    public ResponseEntity<CommentDTO> saveComment(@RequestBody CommentDTO commentDTO){
        return commentService.saveComment(commentDTO);
    }

    @PutMapping("")
    public ResponseEntity<CommentDTO> updateComment(@RequestBody CommentDTO commentDTO){
        return commentService.updateComment(commentDTO);
    }

    @GetMapping("")
    public ResponseEntity<ItemListDTO<CommentDTO>> findAllComments(@RequestParam(required = false) Integer page, @RequestParam(required = false) Boolean old){
        return commentService.findAllComments(page, old);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<HttpStatus> deleteCommentById(@PathVariable int commentId){
        return commentService.deleteCommentById(commentId);
    }


}
