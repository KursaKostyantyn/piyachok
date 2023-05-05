package com.example.piyachok.services;

import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.dao.TopDAO;
import com.example.piyachok.models.Place;
import com.example.piyachok.models.Top;
import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.TopDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TopService {
    private TopDAO topDAO;
    private ItemListService itemListService;
    private PlaceDAO placeDAO;


    public static TopDTO convertTopToTopDTO(Top top) {
        TopDTO topDTO = new TopDTO();
        topDTO.setId(top.getId());
        topDTO.setName(top.getName());
        return topDTO;
    }

    public ResponseEntity<ItemListDTO<TopDTO>> findAllTops(Integer page) {
        int itemsOnPage = 10;
        boolean old=false;
        List<TopDTO> topDTOS = topDAO.findAll().stream().map(TopService::convertTopToTopDTO).collect(Collectors.toList());
        if (topDTOS.size() != 0) {
            return itemListService.createResponseEntity(topDTOS, itemsOnPage, page, old);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<TopDTO> saveTop(TopDTO topDTO) {
        if (topDTO != null) {
            Top top = new Top();
            top.setName(topDTO.getName());
            topDAO.save(top);
            return new ResponseEntity<>(convertTopToTopDTO(top), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<TopDTO> deleteTopById(int topId) {
        if (topId <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Top top = topDAO.findById(topId).orElse(new Top());
        if (top.getId() != 0) {
            topDAO.delete(top);
            return new ResponseEntity<>(convertTopToTopDTO(top), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<TopDTO> updateTopById(int topId, TopDTO topDTO) {
        if (topId <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Top top = topDAO.findById(topId).orElse(new Top());
        if (top.getId() != 0) {
            top.setName(topDTO.getName());
            topDAO.save(top);
            return new ResponseEntity<>(convertTopToTopDTO(top), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<TopDTO> findTopById(int topId) {
        if (topId <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Top top = topDAO.findById(topId).orElse(new Top());
        if (top.getId() != 0) {
            return new ResponseEntity<>(convertTopToTopDTO(top), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<HttpStatus> addPlaceToTopById(int placeId,int topId){
        if (placeId<=0 || topId<=0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Top top=topDAO.findById(topId).orElse(new Top());
        Place place= placeDAO.findById(placeId).orElse(new Place());
        if ( top.getPlaces().contains(place)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if (top.getId()!=0 && place.getId()!=0 ){
            place.getTops().add(top);
            top.getPlaces().add(place);
            topDAO.save(top);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<TopDTO> deletePlaceFromTop(int placeId,int topId){
        if (placeId<=0|| topId<=0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Top top=topDAO.findById(topId).orElse(new Top());
        if (top.getId()!=0){
            top.getPlaces().removeIf(place -> place.getId()==placeId );
            topDAO.save(top);
            return new ResponseEntity<>(convertTopToTopDTO(top),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<TopDTO>> findTopsByPlaceId(int placeId){
        if (placeId<=0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Place place=placeDAO.findById(placeId).orElse(new Place());
        if (place.getId()!=0){
            List<TopDTO> tops=topDAO.findAllByPlacesContaining(place).orElse(new ArrayList<>()).stream().map(TopService::convertTopToTopDTO).collect(Collectors.toList());
            return new ResponseEntity<>(tops,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



}
