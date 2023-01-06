package com.example.piyachok.services;

import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.News;
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

@AllArgsConstructor
@Service
public class PlaceService {

    private PlaceDAO placeDAO;
    private UserDAO userDAO;

    private PlaceDTO convertPlaceToPlaceDTO(Place place) {
        List<Integer> newsIds =new ArrayList<>();
        if(place.getNews().size()!=0){
            newsIds= place.getNews().stream().map(News::getId).collect(Collectors.toList());
        }



        return new PlaceDTO(
                place.getId(),
                place.getName(),
                place.getPhoto(),
                place.getAddress(),
                place.getWorkSchedule(),
                place.isActivated(),
                place.getDescription(),
                place.getContacts(),
                place.getAverageCheck(),
                place.getCreationDate(),
                place.getType(),
                place.getUser().getId(),
                newsIds

        );
    }

    public ResponseEntity<PlaceDTO> savePlace(int userId,Place place) {
        User user=userDAO.findById(userId).orElse(new User());
        System.out.println("id=" + userId + user);
        if (place != null && user.getLogin()!=null) {
            place.setUser(user);
            place.setNews(new ArrayList<>());
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

    public boolean addPlaceNews(int placeId,News news){
        Place place=placeDAO.findById(placeId).orElse(new Place());
        if (place.getName()!=null){
            place.getNews().add(news);
            placeDAO.save(place);
            news.setPlace(place);
            return true;
        }

        return false;
    }


}
