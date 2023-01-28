package com.example.piyachok.services;

import com.example.piyachok.dao.FavoritePlacesDAO;
import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.FavoritePlace;
import com.example.piyachok.models.Place;
import com.example.piyachok.models.User;
import com.example.piyachok.models.dto.PlaceDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FavoritePlacesService {

    private PlaceDAO placeDAO;
    private PlaceService placeService;
    private UserDAO userDAO;
    private FavoritePlacesDAO favoritePlacesDAO;

    public ResponseEntity<List<PlaceDTO>> getFavoritePlacesByUserLogin(String login) {
        User user = userDAO.findUserByLogin(login).orElse(new User());
        if (user.getPlaces() != null) {
            List<PlaceDTO> placeDTO = user.getPlaces()
                    .stream()
                    .map(place -> placeService.convertPlaceToPlaceDTO(place))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(placeDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<HttpStatus> addPlaceToFavoriteByPlaceIdAndUserLogin(int placeId, String login) {
        User user = userDAO.findUserByLogin(login).orElse(new User());
        Place place = placeDAO.findById(placeId).orElse(new Place());
        if (checkPLaceAndUser(place, user)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        FavoritePlace favoritePlace = new FavoritePlace(new ArrayList<>(), new ArrayList<>());
        favoritePlace.getPlaces().add(place);
        favoritePlace.getUsers().add(user);
        user.getFavoritePlacesEntity().add(favoritePlace);
        userDAO.save(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    //
//    public ResponseEntity<Boolean> checkPlaceIsFavoriteByPlaceIdAndUserLogin(int placeId, String login) {
//        User user = userDAO.findUserByLogin(login);
//        if (checkPLaceIdAndUserLogin(placeId, user)) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        FavoritePlace favoritePlaces = favoritePlacesDAO.findAllByUserId(user.getId())
//                .stream()
//                .filter(place -> place.getPlaceId() == placeId)
//                .findFirst().orElse(new FavoritePlace());
//        if (favoritePlaces.getId() != 0) {
//            return new ResponseEntity<>(true, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
//    }
//
    private boolean checkPLaceAndUser(Place place, User user) {
        if (place.getName() == null) {
            return true;
        }
        if (user.getLogin() == null) {
            return true;
        }
        return false;
    }

}
