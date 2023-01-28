package com.example.piyachok.services;

import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.dao.TypeDAO;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.News;
import com.example.piyachok.models.Place;
import com.example.piyachok.models.Type;
import com.example.piyachok.models.User;
import com.example.piyachok.models.dto.ItemListDTO;
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
    private TypeDAO typeDAO;
    private ItemListService itemListService;

    public PlaceDTO convertPlaceToPlaceDTO(Place place) {

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
                place.getTypes(),
                place.getUser().getId(),
                place.getNews()

        );
    }

    public ResponseEntity<PlaceDTO> savePlace(int userId, Place place) {
        User user = userDAO.findById(userId).orElse(new User());
        if (place != null && user.getLogin() != null) {
            place.setUser(user);
            place.setNews(new ArrayList<>());
            List<Type> types=place.getTypes();
            place.setTypes(new ArrayList<>());
            for(Type type:types){
                Type typeByName = typeDAO.getTypeByName(type.getName());
                if (typeByName!=null){
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
            for (Type type : allByPlace){
                type.getPlace().remove(place);
                typeDAO.save(type);
            }
            place.setTypes(new ArrayList<>());
            placeDAO.deleteById(id);
            return new ResponseEntity<>(id,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ItemListDTO<PlaceDTO>> findAllPlaces(Integer page) {
        boolean old=false;
        int itemsOnPage = 10;
        List<PlaceDTO> places = placeDAO.findAll()
                .stream()
                .map(this::convertPlaceToPlaceDTO)
                .collect(Collectors.toList());
      return itemListService.createResponseEntity(places,itemsOnPage,page,old);
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
            //todo delete?
//            placeDAO.save(place);
            news.setPlace(place);
            return true;
        }

        return false;
    }

}
