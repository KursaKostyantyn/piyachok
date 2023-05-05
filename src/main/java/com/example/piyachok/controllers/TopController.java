package com.example.piyachok.controllers;

import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.TopDTO;
import com.example.piyachok.services.TopService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tops")
@AllArgsConstructor
public class TopController {
    private TopService topService;

    @GetMapping("")
    public ResponseEntity<ItemListDTO<TopDTO>> findAllTops(@RequestParam(required = false) Integer page){
        return topService.findAllTops(page);
    }

    @PostMapping("")
    public ResponseEntity<TopDTO> saveTop(@RequestBody TopDTO topDTO){
        return topService.saveTop(topDTO);
    }

    @DeleteMapping("/{topId}")
    public ResponseEntity<TopDTO> deleteTop(@PathVariable int topId){
        return topService.deleteTopById(topId);
    }

    @PutMapping ("/{topId}")
    public ResponseEntity<TopDTO> updateTopById(@PathVariable int topId, @RequestBody TopDTO topDTO){
        return topService.updateTopById(topId,topDTO);
    }

    @GetMapping("/{topId}")
    public ResponseEntity<TopDTO> findTopById(@PathVariable int topId) {
        return topService.findTopById(topId);
    }

    @PutMapping("/addPlace")
    public ResponseEntity<HttpStatus> addPlaceToTopById (@RequestParam int placeId, @RequestParam int topId){
        return topService.addPlaceToTopById(placeId,topId);
    }

    @PutMapping("/deletePlace")
    public ResponseEntity<TopDTO> deletePlaceFromTop(@RequestParam int placeId, @RequestParam int topId){
        return topService.deletePlaceFromTop(placeId, topId);
    }

    @GetMapping("/byPlace/{placeId}")
    public ResponseEntity<List<TopDTO>> findTopsByPlaceId (@PathVariable int placeId){
        return topService.findTopsByPlaceId(placeId);
    }


}
