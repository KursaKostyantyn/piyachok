package com.example.piyachok.controllers;

import com.example.piyachok.models.dto.PlaceDTO;
import com.example.piyachok.services.FavoritePlacesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("myCabinet/favoritePlaces")
public class FavoritePlacesController {
    private FavoritePlacesService favoritePlacesService;

    @GetMapping("")
    public ResponseEntity<List<PlaceDTO>> getFavoritePlacesByUserId (@RequestParam int userId){
        return favoritePlacesService.getFavoritePlacesByUserId(userId);
    }

}
