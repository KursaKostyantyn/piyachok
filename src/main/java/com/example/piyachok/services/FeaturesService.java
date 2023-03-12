package com.example.piyachok.services;

import com.example.piyachok.dao.FeatureDAO;
import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.models.Feature;
import com.example.piyachok.models.Place;
import com.example.piyachok.models.dto.FeatureDTO;
import com.example.piyachok.models.dto.ItemListDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FeaturesService {
    private FeatureDAO featureDAO;
    private ItemListService itemListService;
    private PlaceDAO placeDAO;

    public static FeatureDTO convertFeatureToFeatureDTO(Feature feature){
        FeatureDTO featureDTO = new FeatureDTO();
        featureDTO.setId(feature.getId());
        featureDTO.setName(feature.getName());
        return featureDTO;
    }

    public ResponseEntity<ItemListDTO<FeatureDTO>> findAllFeatures(Integer page){
        List<FeatureDTO> features = featureDAO.findAll().stream().map(FeaturesService::convertFeatureToFeatureDTO).collect(Collectors.toList());
        boolean old = false;
        int itemsOnPage = 10;
        return itemListService.createResponseEntity(features,itemsOnPage,page,old);
    }

    public ResponseEntity<FeatureDTO> findFeatureById(int featureId){
        Feature feature=featureDAO.findFeatureById(featureId).orElse(new Feature());
        if (feature.getId()!=0){
            return new ResponseEntity<>(convertFeatureToFeatureDTO(feature), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<FeatureDTO> saveFeature(Feature feature){
        if (feature!=null){
            featureDAO.save(feature);
            return new ResponseEntity<>(convertFeatureToFeatureDTO(feature),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<FeatureDTO> updateFeatureById(FeatureDTO featureDTO,int id){
        Feature feature=featureDAO.findFeatureById(id).orElse(new Feature());
        if (feature.getId()!=0){
            feature.setName(featureDTO.getName());
            featureDAO.save(feature);
            return new ResponseEntity<>(convertFeatureToFeatureDTO(feature),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<HttpStatus> deleteFeatureById(int featureId){
        Feature feature=featureDAO.findFeatureById(featureId).orElse(new Feature());


        if (feature.getId()!=0){
            List<Place> places=placeDAO.findAllByFeaturesContaining(feature);
            for (Place place:places){
                place.getFeatures().remove(feature);
                placeDAO.save(place);
            }
            featureDAO.delete(feature);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
