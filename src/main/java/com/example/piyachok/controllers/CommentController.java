package com.example.piyachok.controllers;

import com.example.piyachok.models.dto.CommentDTO;
import com.example.piyachok.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("myCabinet")
public class CommentController {

    private CommentService commentService;

    @GetMapping("/myComments")
    public ResponseEntity<List<CommentDTO>> findCommentsByUserLogin (@RequestParam String login){
        return commentService.findCommentsByUserLogin(login);
    }
}
