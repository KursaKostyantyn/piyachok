package com.example.piyachok.controllers;

import com.example.piyachok.models.dto.RatingDTO;
import com.example.piyachok.services.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("")
public class RatingController {
    private RatingService ratingService;

    @GetMapping("/myCabinet/allRatings")
    public ResponseEntity<List<RatingDTO>> findAllRatings() {
        return ratingService.findAllRatings();
    }

    @GetMapping("/myCabinet/myRatings")
    public ResponseEntity<List<RatingDTO>> findRatingsByUserLogin(String login) {
        return ratingService.findRatingsByUserLogin(login);
    }

    @PostMapping("ratings")
    public ResponseEntity<RatingDTO> saveRating(@RequestBody RatingDTO ratingDTO) {
        return ratingService.saveRating(ratingDTO);
    }

    @GetMapping("ratings/rating")
    public ResponseEntity<RatingDTO> findRatingByPLaceIdAndUserLogin(@RequestParam int placeId, @RequestParam String userLogin) {
        return ratingService.findRatingByPLaceIdAndUserLogin(placeId, userLogin);
    }

    @PutMapping("ratings")
    public ResponseEntity<RatingDTO> updateRating(@RequestBody RatingDTO ratingDTO) {
        return ratingService.updateRating(ratingDTO);
    }

    @GetMapping("ratings/{myRatingsId}")
    public ResponseEntity<RatingDTO> findRatingById(@PathVariable int myRatingsId){
        return ratingService.findRatingById(myRatingsId);
    }


}
