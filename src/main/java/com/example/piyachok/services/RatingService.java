package com.example.piyachok.services;

import com.example.piyachok.dao.RatingDAO;
import com.example.piyachok.models.Rating;
import com.example.piyachok.models.dto.RatingDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RatingService {
    private RatingDAO ratingDAO;

    public RatingDTO convertRatingToRatingDTO(Rating rating) {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setId(rating.getId());
        ratingDTO.setRating(rating.getRating());
        ratingDTO.setUser(rating.getUser());
        ratingDTO.setPlace(rating.getPlace());
        return ratingDTO;
    }

    public ResponseEntity<List<RatingDTO>> findAllRatings() {
        List<RatingDTO> ratingsDTO = ratingDAO.findAll()
                .stream()
                .map(this::convertRatingToRatingDTO)
                .collect(Collectors.toList());
        if (ratingsDTO.size() != 0) {
            return new ResponseEntity<>(ratingsDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<RatingDTO>> findRatingsByUserLogin(String login) {
        List<Rating> userRatings = ratingDAO.findAllByUser_Login(login);

        for (Rating rating : userRatings) {
            System.out.println(rating);
        }

        if (userRatings.size() != 0) {
            return new ResponseEntity<>(userRatings
                    .stream()
                    .map(this::convertRatingToRatingDTO)
                    .collect(Collectors.toList()), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
