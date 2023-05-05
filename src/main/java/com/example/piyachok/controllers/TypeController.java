package com.example.piyachok.controllers;

import com.example.piyachok.models.Type;
import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.TypeDTO;
import com.example.piyachok.services.TypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/types")
public class TypeController {
    private TypeService typeService;

    @GetMapping("")
    public ResponseEntity<ItemListDTO<TypeDTO>> findAllTypes(@RequestParam(required = false) Integer page, @RequestParam(required = false) Boolean old) {
        return typeService.findAllTypes(page, old);
    }

    @PostMapping("")
    public ResponseEntity<TypeDTO> saveType(@RequestBody Type type) {
        return typeService.saveType(type);
    }

    @GetMapping("/{typeId}")
    public ResponseEntity<TypeDTO> findTypeById(@PathVariable int typeId) {
        return typeService.findTypeById(typeId);
    }

    @PutMapping("/{typeId}")
    public ResponseEntity<TypeDTO> updateType(@PathVariable int typeId, @RequestBody TypeDTO typeDTO) {
        return typeService.updateTypeById(typeId, typeDTO);
    }

    @DeleteMapping("/{typeId}")
    public ResponseEntity<Integer> deleteTypeById(@PathVariable int typeId) {
        return typeService.deleteTypeById(typeId);
    }



}
