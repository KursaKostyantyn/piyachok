package com.example.piyachok.services;

import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.models.Place;
import com.example.piyachok.models.dto.PlaceDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PlaceService {

    private PlaceDAO placeDAO;

    private PlaceDTO convertPlaceToPlaceDTO(Place place) {
        return new PlaceDTO(
                place.getId(),
                place.getName(),
                place.getPhoto(),
                place.getAddress(),
                place.getSchedule(),
                place.isActivated(),
                place.getDescription(),
                place.getContacts(),
                place.getAverageCheck(),
                place.getCreationDate(),
                place.getType(),
                place.getUser()
        );
    }

    public ResponseEntity<PlaceDTO> savePlace(Place place) {
        if (place != null) {
            placeDAO.save(place);
            return new ResponseEntity<>(convertPlaceToPlaceDTO(place), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<HttpStatus> deletePlaceById(int id) {
        Place place = placeDAO.findById(id).orElse(new Place());
        if (place.getName() != null) {
            placeDAO.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<PlaceDTO>> findAllPlaces() {
        return new ResponseEntity<>(placeDAO.findAll()
                .stream()
                .map(this::convertPlaceToPlaceDTO)
                .collect(Collectors.toList()), HttpStatus.OK);

    }


    public ResponseEntity<PlaceDTO> findPlaceById(int id) {
        Place place = placeDAO.findById(id).orElse(new Place());
        if (place.getName() != null) {
            return new ResponseEntity<>(convertPlaceToPlaceDTO(place), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<PlaceDTO> updatePlaceById(int id, Place place) {
        Place oldPlace = placeDAO.findById(id).orElse(new Place());
        if (oldPlace.getName() != null) {
            place.setId(id);
            placeDAO.save(place);
            return new ResponseEntity<>(convertPlaceToPlaceDTO(place), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
