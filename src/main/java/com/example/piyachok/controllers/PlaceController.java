package com.example.piyachok.controllers;

import com.example.piyachok.models.Place;
import com.example.piyachok.models.dto.PlaceDTO;
import com.example.piyachok.services.PlaceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/places")
public class PlaceController {

    private PlaceService placeService;

    @PostMapping("")
    public ResponseEntity<PlaceDTO> savePlace(@RequestBody Place place) {
        return placeService.savePlace(place);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePlaceById(@PathVariable int id) {
        return placeService.deletePlaceById(id);
    }

    @GetMapping("")
    public ResponseEntity<List<PlaceDTO>> findAllPlaces() {
        return placeService.findAllPlaces();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDTO> findPlaceById(@PathVariable int id) {
        return placeService.findPlaceById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceDTO> updatePlaceById(@PathVariable int id, @RequestBody Place place) {
        return placeService.updatePlaceById(id, place);
    }

}
