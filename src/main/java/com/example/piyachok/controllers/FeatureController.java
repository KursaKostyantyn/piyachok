package com.example.piyachok.controllers;

import com.example.piyachok.models.Feature;
import com.example.piyachok.models.dto.FeatureDTO;
import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.services.FeaturesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("features")
public class FeatureController {
    private FeaturesService featuresService;

    @GetMapping("")
    public ResponseEntity<ItemListDTO<FeatureDTO>> findAllFeatures(@RequestParam(required = false) Integer page) {
        return featuresService.findAllFeatures(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeatureDTO> findFeatureById(@PathVariable int id) {
        return featuresService.findFeatureById(id);
    }

    @PostMapping("")
    public ResponseEntity<FeatureDTO> saveFeature(@RequestBody Feature feature) {
        return featuresService.saveFeature(feature);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeatureDTO> updateDeatureById(@PathVariable int id, @RequestBody FeatureDTO featureDTO) {
        return featuresService.updateFeatureById(featureDTO, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteFeatureById(@PathVariable int id){
        return featuresService.deleteFeatureById(id);
    }


}
