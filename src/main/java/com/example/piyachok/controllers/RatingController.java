package com.example.piyachok.controllers;

import com.example.piyachok.models.dto.RatingDTO;
import com.example.piyachok.services.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/myCabinet")
public class RatingController {
    private RatingService ratingService;

    @GetMapping("/allRatings")
    public ResponseEntity<List<RatingDTO>> findAllRatings(){
        return  ratingService.findAllRatings();
    }

    @GetMapping("/myRatings")
    public ResponseEntity<List<RatingDTO>> findRatingsByUserLogin(String login){
       return ratingService.findRatingsByUserLogin(login);
    }

}
