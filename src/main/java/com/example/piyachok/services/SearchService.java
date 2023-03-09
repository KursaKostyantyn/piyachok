package com.example.piyachok.services;

import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.dao.TypeDAO;
import com.example.piyachok.models.Place;
import com.example.piyachok.models.Type;
import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.PlaceDTO;
import com.example.piyachok.models.dto.TypeDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchService {
    private PlaceDAO placeDAO;
    private TypeDAO typeDAO;
    private ItemListService itemListService;




    public List<Type> getAllTypes(List<Integer> typesId) {
        List<Type> types = new ArrayList<>();
        for (Integer integer:typesId) {
            Type type = typeDAO.findById(integer).orElse(new Type());
            if (type.getId() != 0) {
                types.add(type);
            }
        }
        return types;
    }

    public ResponseEntity<ItemListDTO<PlaceDTO>> findPLaceByTypes(List<Integer> typesId, Integer page, Boolean old) {
        int itemsOnPage = 10;
        List<Place> places = new ArrayList<>();
        List<Type> types = getAllTypes(typesId);
        if (types.size() != 0) {
            for (Type type : types) {
                List<Place> allByTypesContains = placeDAO.findAllByTypesContains(type);
                if (allByTypesContains.size() != 0) {
                    places.addAll(allByTypesContains);
                }
            }
        }

        if (places.size() != 0) {
            List<PlaceDTO> convertedPlaces = places
                    .stream()
                    .map(PlaceService::convertPlaceToPlaceDTO)
                    .collect(Collectors.toList());
            return itemListService.createResponseEntity(convertedPlaces, itemsOnPage, page, old);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
