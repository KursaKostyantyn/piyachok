package com.example.piyachok.controllers;

import com.example.piyachok.models.Place;
import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.PlaceDTO;
import com.example.piyachok.services.PlaceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;

@AllArgsConstructor
@RestController
@RequestMapping("/places")
public class PlaceController {

    private PlaceService placeService;

    @PostMapping("")
    public ResponseEntity<PlaceDTO> savePlace(@RequestParam int userId,@RequestBody Place place) {
        return placeService.savePlace(userId,place);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deletePlaceById(@PathVariable int id) {
        return placeService.deletePlaceById(id);
    }

    @GetMapping("")
    public ResponseEntity<ItemListDTO<PlaceDTO>> findAllPlaces(@RequestParam(required = false) int page) {
        System.out.println(page);
        return placeService.findAllPlaces(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDTO> findPlaceById(@PathVariable int id) {
        return placeService.findPlaceById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceDTO> updatePlaceById(@PathVariable int id, @RequestBody Place place) {
        return placeService.updatePlaceById(id, place);
    }

    @GetMapping("myPlaces")
    public ResponseEntity<ItemListDTO<PlaceDTO>> findPlacesByUserLogin(@RequestParam(required = false) Integer page,@RequestParam String userLogin){
        return placeService.findPlacesByUserLogin(page,userLogin);
    }


}
