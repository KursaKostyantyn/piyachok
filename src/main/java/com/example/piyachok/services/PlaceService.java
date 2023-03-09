package com.example.piyachok.services;

import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.dao.TypeDAO;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.*;
import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.PlaceDTO;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        placeDTO.setPhotos(place.getPhotos());
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

    public ResponseEntity<PlaceDTO> savePlaceWithPhoto(int userId, Place place) {


        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<Integer> deletePlaceById(int id) {
        Place place = placeDAO.findById(id).orElse(new Place());
        if (place.getName() != null) {
            List<Type> allByPlace = typeDAO.findAllByPlacesContaining(place);
            for (Type type : allByPlace) {
                type.getPlaces().remove(place);
                typeDAO.save(type);
            }
            List<User> users = userDAO.findAllByFavoritePlaces(place).orElse(new ArrayList<>());
            if (users.size() != 0) {
                for (User user : users) {
                    user.getFavoritePlaces().remove(place);
                    userDAO.save(user);
                }
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

    public ResponseEntity<ItemListDTO<PlaceDTO>> findAllActivatedPlaces(Integer page,
                                                                        Boolean alphabet,
                                                                        Boolean old,
                                                                        Boolean rating,
                                                                        Boolean averageCheck) {
        System.out.println(rating);
        int itemsOnPage = 10;
        List<PlaceDTO> places = placeDAO.findAllByActivatedTrue()
                .stream()
                .map(PlaceService::convertPlaceToPlaceDTO)
                .collect(Collectors.toList());
        if (alphabet != null) {
            places = sortByAlphabet(places, alphabet);
        }
        if (rating != null) {
            places = sortByRating(places, rating);
        }
        if (averageCheck != null) {
            places = sortByAverageCheck(places, averageCheck);
        }
        if (places.size() != 0) {
            return itemListService.createResponseEntity(places, itemsOnPage, page, old);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private List<PlaceDTO> sortByAlphabet(List<PlaceDTO> places, boolean alphabet) {
        if (alphabet) {
            places = places
                    .stream()
                    .sorted((Comparator.comparing(PlaceDTO::getName)))
                    .collect(Collectors.toList());
        } else {
            places = places
                    .stream()
                    .sorted((Comparator.comparing(PlaceDTO::getName).reversed()))
                    .collect(Collectors.toList());
        }
        return places;
    }

    private List<PlaceDTO> sortByRating(List<PlaceDTO> places, boolean rating) {
        System.out.println(rating);
        if (rating) {
            places = places
                    .stream()
                    .sorted(Comparator.comparingDouble(PlaceDTO::getAverageRating))
                    .collect(Collectors.toList());
        } else {
            places = places
                    .stream()
                    .sorted(Comparator.comparingDouble(PlaceDTO::getAverageRating).reversed())
                    .collect(Collectors.toList());
        }
        return places;
    }

    private List<PlaceDTO> sortByAverageCheck(List<PlaceDTO> places, boolean averageCheck) {
        if (averageCheck) {
            places = places
                    .stream()
                    .sorted(Comparator.comparingInt(PlaceDTO::getAverageCheck))
                    .collect(Collectors.toList());
        } else {
            places = places
                    .stream()
                    .sorted(Comparator.comparingInt(PlaceDTO::getAverageCheck).reversed())
                    .collect(Collectors.toList());
        }
        return places;
    }

    public ResponseEntity<ItemListDTO<PlaceDTO>> findAllNotActivatedPlaces(Integer page) {
        boolean old = false;
        int itemsOnPage = 10;
        List<PlaceDTO> places = placeDAO.findAllByActivatedFalse()
                .stream()
                .map(PlaceService::convertPlaceToPlaceDTO)
                .collect(Collectors.toList());
        if (places.size() != 0) {
            return itemListService.createResponseEntity(places, itemsOnPage, page, old);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

    public ResponseEntity<ItemListDTO<PlaceDTO>> findPlacesByUserLogin(Integer page, String userLogin) {
        boolean old = false;
        int itemsOnPage = 10;
        List<Place> places = placeDAO.findAllByUser_Login(userLogin).orElse(new ArrayList<>());
        if (places.size() != 0) {
            List<PlaceDTO> placeDTOS = places
                    .stream()
                    .map(PlaceService::convertPlaceToPlaceDTO)
                    .collect(Collectors.toList());
            return itemListService.createResponseEntity(placeDTOS, itemsOnPage, page, old);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<PlaceDTO> addPhotosToPlaceById(int placeId, @NonNull List<MultipartFile> photos) {
        Place place = placeDAO.findById(placeId).orElse(new Place());

        if (place.getId() != 0) {
            File placesPhoto = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator + "placesPhoto");
            if (!placesPhoto.exists()) {
                placesPhoto.mkdir();
            }
            for (MultipartFile photo : photos) {
                String originalFilename = photo.getOriginalFilename();
                place.getPhotos().add(originalFilename);
                placeDAO.save(place);
                String pathToSavePhoto = placesPhoto.getAbsolutePath() + File.separator + originalFilename;
                try {
                    photo.transferTo(new File(pathToSavePhoto));
                } catch (IOException e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>(convertPlaceToPlaceDTO(place), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ItemListDTO<PlaceDTO>> findPLaceByName(String placeName, Integer page, Boolean old) {
        int itemsOnPage = 10;
        List<Place> places = placeDAO.findAll();
        if (places.size() != 0) {
            List<PlaceDTO> placeDTOS = new ArrayList<>();
            for (Place place : places) {
                if (place.getName().toLowerCase().contains(placeName.toLowerCase())) {
                    placeDTOS.add(PlaceService.convertPlaceToPlaceDTO(place));
                }
            }
            return itemListService.createResponseEntity(placeDTOS, itemsOnPage, page, old);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
