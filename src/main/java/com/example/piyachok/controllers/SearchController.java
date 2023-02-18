package com.example.piyachok.controllers;

import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.PlaceDTO;
import com.example.piyachok.models.dto.TypeDTO;
import com.example.piyachok.services.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("search")
public class SearchController {
    private SearchService searchService;

    @GetMapping("/findPLaceByName")
    public ResponseEntity<ItemListDTO<PlaceDTO>> findPLaceByName(@RequestParam String placeName, @RequestParam(required = false) Integer page,@RequestParam(required = false) Boolean old){
        return searchService.findPLaceByName(placeName,page,old);
    }

    @GetMapping("/findPLaceByTypesId")
    public ResponseEntity<ItemListDTO<PlaceDTO>> findPLaceByTypes(@RequestParam List<Integer> typesId, @RequestParam(required = false) Integer page, @RequestParam(required = false) Boolean old){
        return searchService.findPLaceByTypes(typesId,page,old);
    }


}
