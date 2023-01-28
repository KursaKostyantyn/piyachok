package com.example.piyachok.controllers;

import com.example.piyachok.models.Type;
import com.example.piyachok.models.dto.TypeDTO;
import com.example.piyachok.services.TypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/types")
public class TypeController {
    private TypeService typeService;

    @GetMapping("")
    public ResponseEntity<List<TypeDTO>> findAllTypes() {
        return typeService.findAllTypes();
    }

    @PostMapping("")
    public ResponseEntity<TypeDTO> saveType(@RequestBody Type type){
        return typeService.saveType(type);
    }

}
