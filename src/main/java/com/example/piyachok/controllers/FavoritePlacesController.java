package com.example.piyachok.controllers;

import com.example.piyachok.models.dto.PlaceDTO;
import com.example.piyachok.services.FavoritePlacesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("myCabinet/favoritePlaces")
public class FavoritePlacesController {
    private FavoritePlacesService favoritePlacesService;

    @GetMapping("")
    public ResponseEntity<List<PlaceDTO>> getFavoritePlacesByUserId(@RequestParam String login) {
        return favoritePlacesService.getFavoritePlacesByUserLogin(login);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> addPlaceToFavoriteByPlaceIdAndUserLogin(@RequestParam int placeId, @RequestParam String login) {
        return favoritePlacesService.addPlaceToFavoriteByPlaceIdAndUserLogin(placeId, login);
    }

//    @GetMapping("checkPlaceIsFavorite")
//    public ResponseEntity<Boolean> checkPlaceIsFavoriteByPlaceIdAndUserLogin(@RequestParam int placeId,@RequestParam String login){
//        return favoritePlacesService.checkPlaceIsFavoriteByPlaceIdAndUserLogin(placeId,login);
//    }

}
