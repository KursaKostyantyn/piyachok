package com.example.piyachok.controllers;

import com.example.piyachok.models.Place;
import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.PlaceDTO;
import com.example.piyachok.services.PlaceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/places")
public class PlaceController {

    private PlaceService placeService;

    @PostMapping("")
    public ResponseEntity<PlaceDTO> savePlace(@RequestParam int userId, @RequestBody Place place) {
        return placeService.savePlace(userId, place);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deletePlaceById(@PathVariable int id) {
        return placeService.deletePlaceById(id);
    }

    @GetMapping("")
    public ResponseEntity<ItemListDTO<PlaceDTO>> findAllPlaces(@RequestParam(required = false) int page) {
        return placeService.findAllPlaces(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDTO> findPlaceById(@PathVariable int id) {
        return placeService.findPlaceById(id);
    }

    @PutMapping("/{placeId}")
    public ResponseEntity<PlaceDTO> updatePlaceById(@PathVariable int placeId, @RequestBody Place place) {
        return placeService.updatePlaceById(placeId, place);
    }

    @GetMapping("myPlaces")
    public ResponseEntity<ItemListDTO<PlaceDTO>> findPlacesByUserLogin(@RequestParam(required = false) Integer page, @RequestParam String userLogin) {
        return placeService.findPlacesByUserLogin(page, userLogin);
    }

    @GetMapping("/activated")
    public ResponseEntity<ItemListDTO<PlaceDTO>> findAllActivatedPlaces(@RequestParam(required = false) Integer page,
                                                                        @RequestParam(required = false) Boolean alphabet,
                                                                        @RequestParam(required = false) Boolean old,
                                                                        @RequestParam(required = false) Boolean rating,
                                                                        @RequestParam(required = false) Boolean averageCheck) {
        return placeService.findAllActivatedPlaces(page, alphabet, old, rating, averageCheck);
    }

    @GetMapping("/notActivated")
    public ResponseEntity<ItemListDTO<PlaceDTO>> findAllNotActivatedPlaces(@RequestParam(required = false) Integer page) {
        return placeService.findAllNotActivatedPlaces(page);
    }

    @PutMapping("/addPhotos")
    public ResponseEntity<PlaceDTO> addPhotosToPlaceById(@RequestParam int placeId, @RequestParam List<MultipartFile> photos) {
        return placeService.addPhotosToPlaceById(placeId, photos);
    }

    @GetMapping("search/findPLaceByName")
    public ResponseEntity<ItemListDTO<PlaceDTO>> findPLaceByName(@RequestParam String placeName, @RequestParam(required = false) Integer page, @RequestParam(required = false) Boolean old) {
        return placeService.findPLaceByName(placeName, page, old);
    }

}
