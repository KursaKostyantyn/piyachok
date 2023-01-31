package com.example.piyachok.services;

import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.dao.RatingDAO;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.Place;
import com.example.piyachok.models.Rating;
import com.example.piyachok.models.User;
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
    private PlaceDAO placeDAO;
    private UserDAO userDAO;

    public RatingDTO convertRatingToRatingDTO(Rating rating) {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setId(rating.getId());
        ratingDTO.setRating(rating.getRating());
        ratingDTO.setUserLogin(rating.getUser().getLogin());
        ratingDTO.setPlaceId(rating.getPlace().getId());
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

    public ResponseEntity<RatingDTO> saveRating(RatingDTO ratingDTO) {
        Rating rating = checkPlaceIdAndUserLogin(ratingDTO);
        if (rating.getPlace() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (checkIsExistRating(ratingDTO).getPlace()==null){
            ratingDAO.save(rating);
            return new ResponseEntity<>(convertRatingToRatingDTO(rating),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    private Rating checkPlaceIdAndUserLogin(RatingDTO ratingDTO) {
        Place place = placeDAO.findById(ratingDTO.getPlaceId()).orElse(new Place());
        Rating rating = new Rating();
        if (place.getName() == null) {
            return rating;
        }

        User user = userDAO.findUserByLogin(ratingDTO.getUserLogin()).orElse(new User());
        if (user.getLogin() == null) {
            return rating;
        }
        rating.setUser(user);
        rating.setRating(ratingDTO.getRating());
        rating.setPlace(place);
        return rating;
    }

    private Rating checkIsExistRating(RatingDTO ratingDTO) {
        List<Rating> ratings = ratingDAO.findAllByPlaceId(ratingDTO.getPlaceId());
        Rating matchRating = ratings
                .stream()
                .filter(value -> value.getUser().getLogin().equals(ratingDTO.getUserLogin()))
                .findFirst()
                .orElse(new Rating());
        return matchRating;

    }



    public ResponseEntity<RatingDTO> findRatingByPLaceIdAndUserLogin(int placeId, String userLogin){
        Rating rating = ratingDAO.findRatingByPlaceIdAndUserLogin(placeId, userLogin).orElse(new Rating());
        if (rating.getPlace()!=null){
            return new ResponseEntity<>(convertRatingToRatingDTO(rating),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<RatingDTO> updateRating(RatingDTO ratingDTO){
        Rating rating = ratingDAO.findRatingByPlaceIdAndUserLogin(ratingDTO.getPlaceId(), ratingDTO.getUserLogin()).orElse(new Rating());
        if (rating.getPlace()!=null){
            rating.setRating(ratingDTO.getRating());
            ratingDAO.save(rating);
            return new ResponseEntity<>(convertRatingToRatingDTO(rating),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
