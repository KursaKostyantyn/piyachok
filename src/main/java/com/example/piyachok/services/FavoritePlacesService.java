package com.example.piyachok.services;

import com.example.piyachok.dao.FavoritePlacesDAO;
import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.models.FavoritePlaces;
import com.example.piyachok.models.Place;
import com.example.piyachok.models.dto.PlaceDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FavoritePlacesService {

    private PlaceDAO placeDAO;
    private PlaceService placeService;
    private FavoritePlacesDAO favoritePlacesDAO;

    public ResponseEntity<List<PlaceDTO>> getFavoritePlacesByUserId(int userId) {
        List<PlaceDTO> places = new ArrayList<>();
        List<FavoritePlaces> favoritePlaces = favoritePlacesDAO.findAllByUserId(userId);
        for (FavoritePlaces favorite : favoritePlaces) {
            places.add(placeService.convertPlaceToPlaceDTO(placeDAO.findById(favorite.getPlaceId()).orElse(new Place())));
        }
        return new ResponseEntity<>(places, HttpStatus.OK);
    }


}
