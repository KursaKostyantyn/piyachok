package com.example.piyachok.services;

import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.dao.TypeDAO;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.*;
import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.PlaceDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class PlaceService {

    private PlaceDAO placeDAO;
    private UserDAO userDAO;
    private TypeDAO typeDAO;
    private ItemListService itemListService;

    public static PlaceDTO convertPlaceToPlaceDTO(Place place) {
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setId(place.getId());
        placeDTO.setName(place.getName());
        placeDTO.setPhoto(place.getPhoto());
        placeDTO.setAddress(place.getAddress());
        placeDTO.setWorkSchedule(place.getWorkSchedule());
        placeDTO.setActivated(place.isActivated());
        placeDTO.setDescription(place.getDescription());
        placeDTO.setContacts(place.getContacts());
        placeDTO.setAverageCheck(place.getAverageCheck());
        placeDTO.setCreationDate(place.getCreationDate());
        placeDTO.setTypes(place.getTypes());
        placeDTO.setUserId(place.getUser().getId());
        placeDTO.setNews(place.getNews());
        placeDTO.setAverageRating(calculateAverageRating(place.getRatings()));
        return placeDTO;
    }

    public static double calculateAverageRating(List<Rating> ratings) {
        double average = 0;
        if (ratings.size() != 0) {
            average = ratings.stream().mapToDouble(Rating::getRating).sum() / ratings.size();
            average = Math.round(average * 100.00) / 100.00;
            return average;
        }
        return average;
    }

    public ResponseEntity<PlaceDTO> savePlace(int userId, Place place) {

        User user = userDAO.findById(userId).orElse(new User());
        if (place != null && user.getLogin() != null) {
            place.setUser(user);
            place.setNews(new ArrayList<>());
            List<Type> types = place.getTypes();
            place.setTypes(new ArrayList<>());
            for (Type type : types) {
                Type typeByName = typeDAO.getTypeByName(type.getName());
                if (typeByName != null) {
                    place.getTypes().add(typeByName);
                }
            }
            placeDAO.save(place);
            return new ResponseEntity<>(convertPlaceToPlaceDTO(place), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Integer> deletePlaceById(int id) {
        Place place = placeDAO.findById(id).orElse(new Place());
        if (place.getName() != null) {
            List<Type> allByPlace = typeDAO.findAllByPlace(place);
            for (Type type : allByPlace) {
                type.getPlace().remove(place);
                typeDAO.save(type);
            }
            place.setTypes(new ArrayList<>());
            placeDAO.deleteById(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ItemListDTO<PlaceDTO>> findAllPlaces(Integer page) {
        boolean old = false;
        int itemsOnPage = 10;
        List<PlaceDTO> places = placeDAO.findAll()
                .stream()
                .map(PlaceService::convertPlaceToPlaceDTO)
                .collect(Collectors.toList());
        return itemListService.createResponseEntity(places, itemsOnPage, page, old);
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

    public boolean addNewsToPlace(int placeId, News news) {
        Place place = placeDAO.findById(placeId).orElse(new Place());
        if (place.getName() != null) {
            place.getNews().add(news);
            news.setPlace(place);
            return true;
        }

        return false;
    }

}
