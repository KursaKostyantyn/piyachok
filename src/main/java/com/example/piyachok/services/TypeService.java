package com.example.piyachok.services;

import com.example.piyachok.dao.TypeDAO;
import com.example.piyachok.models.Type;
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

    public TypeDTO convertTypeToTypeDTO(Type type){
        TypeDTO typeDTO=new TypeDTO();
        typeDTO.setId(type.getId());
        typeDTO.setName(type.getName());
        return typeDTO;
    }

    public ResponseEntity<List<TypeDTO>> findAllTypes(){
        List<TypeDTO> types = typeDAO.findAll().stream().map(this::convertTypeToTypeDTO).collect(Collectors.toList());
        if(types.size()!=0){
            return new ResponseEntity<>(types, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<TypeDTO> saveType(Type type){
        if(type!=null){
            type.setPlace(new ArrayList<>());
            typeDAO.save(type);
            return new ResponseEntity<>(convertTypeToTypeDTO(type),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
