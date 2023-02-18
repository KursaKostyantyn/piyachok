package com.example.piyachok.services;

import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.dao.TypeDAO;
import com.example.piyachok.models.Place;
import com.example.piyachok.models.Type;
import com.example.piyachok.models.dto.ItemListDTO;
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
public class TypeService {
    private TypeDAO typeDAO;
    private ItemListService itemListService;
    private PlaceDAO placeDAO;

    public static TypeDTO convertTypeToTypeDTO(Type type) {
        TypeDTO typeDTO = new TypeDTO();
        typeDTO.setId(type.getId());
        typeDTO.setName(type.getName());
        return typeDTO;
    }

    public ResponseEntity<ItemListDTO<TypeDTO>> findAllTypes(Integer page, Boolean old) {
        int itemsOnPage = 2;
        List<TypeDTO> types = typeDAO.findAll().stream().map(TypeService::convertTypeToTypeDTO).collect(Collectors.toList());
        if (types.size() != 0) {
            return itemListService.createResponseEntity(types, itemsOnPage, page, old);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<TypeDTO> saveType(Type type) {
        if (type != null) {
            type.setPlace(new ArrayList<>());
            typeDAO.save(type);
            return new ResponseEntity<>(convertTypeToTypeDTO(type), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<TypeDTO> findTypeById(int typeId) {
        Type type = typeDAO.findById(typeId).orElse(new Type());
        if (type.getName() != null) {
            return new ResponseEntity<>(convertTypeToTypeDTO(type), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<TypeDTO> updateType(int typeId, TypeDTO typeDTO ){
        Type type=typeDAO.findById(typeId).orElse(new Type());
        if (type.getId()!=0){
            type.setName(typeDTO.getName());
            typeDAO.save(type);
            return new ResponseEntity<>(convertTypeToTypeDTO(type),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Integer> deleteTypeById(int typeId){
        Type type=typeDAO.findById(typeId).orElse(new Type());
        List<Place> places = type.getPlace();
        for (Place place:places){
            place.getTypes().remove(type);
        }
        placeDAO.saveAll(places);
        if (type.getId()!=0){
            typeDAO.delete(type);
            return new ResponseEntity<>(typeId,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
