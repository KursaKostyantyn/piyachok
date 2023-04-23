package com.example.piyachok.services;

import com.example.piyachok.dao.PiyachokDAO;
import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.models.Piaychok;
import com.example.piyachok.models.Place;
import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.PiyachokDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PiyachokService {
    private PiyachokDAO piyachokDAO;
    private ItemListService itemListService;
    private PlaceDAO placeDAO;

    public static PiyachokDTO convertPiyachokToPiyachokDTO(Piaychok piyachok) {
        String pattern = "yyyy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        PiyachokDTO piyachokDTO = new PiyachokDTO();
        piyachokDTO.setDate(simpleDateFormat.format(piyachok.getDate()));
        piyachokDTO.setAmountOfGuests(piyachok.getAmountOfGuests());
        piyachokDTO.setId(piyachok.getId());
        piyachokDTO.setDesireExpenses(piyachok.getDesireExpenses());
        piyachokDTO.setPlaceId(piyachok.getPlace().getId());
        piyachokDTO.setWriteToMe(piyachok.getWriteToMe());
        piyachokDTO.setMeetingDescription(piyachok.getMeetingDescription());
        return piyachokDTO;
    }

    public static Piaychok convertPiyachokDTOToPiyachok(PiyachokDTO piyachokDTO) throws ParseException {
        Piaychok piaychok = new Piaychok();
        String pattern = "yyyy-MM-dd HH:mm";
        String dateFromUser = piyachokDTO.getDate();
        Date date = new SimpleDateFormat(pattern).parse(dateFromUser);
        piaychok.setMeetingDescription(piyachokDTO.getMeetingDescription());
        piaychok.setWriteToMe(piyachokDTO.getWriteToMe());
        piaychok.setDesireExpenses(piyachokDTO.getDesireExpenses());
        piaychok.setAmountOfGuests(piyachokDTO.getAmountOfGuests());
        piaychok.setDate(date);
        piaychok.setId(piyachokDTO.getId());
        return piaychok;
    }

    public ResponseEntity<ItemListDTO<PiyachokDTO>> findAllPiyachok(Integer page, Boolean old) {
        int itemsOnPage = 10;
        List<PiyachokDTO> piyachokDTOS = piyachokDAO.findAll()
                .stream()
                .map(PiyachokService::convertPiyachokToPiyachokDTO)
                .collect(Collectors.toList());
        if (piyachokDTOS.size() != 0) {
            return itemListService.createResponseEntity(piyachokDTOS, itemsOnPage, page, old);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<PiyachokDTO> savePiyachok(PiyachokDTO piyachokDTO) {
        if (piyachokDTO != null) {
            if (piyachokDTO.getPlaceId() == 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Place place = placeDAO.findById(piyachokDTO.getPlaceId()).orElse(new Place());
            if (place.getId() == 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            try {
                Piaychok piyachok = convertPiyachokDTOToPiyachok(piyachokDTO);
                piyachok.setPlace(place);
                piyachokDAO.save(piyachok);
                return new ResponseEntity<>(convertPiyachokToPiyachokDTO(piyachok), HttpStatus.OK);
            } catch (ParseException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<HttpStatus> deletePiyachokById(int piyachokId) {
        Piaychok piaychok = piyachokDAO.findById(piyachokId).orElse(new Piaychok());
        if (piaychok.getId() != 0) {
            piyachokDAO.delete(piaychok);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<PiyachokDTO> findPiyachokById(int piyachokId) {
        Piaychok piaychok = piyachokDAO.findById(piyachokId).orElse(new Piaychok());
        if (piaychok.getId() != 0) {
            return new ResponseEntity<>(convertPiyachokToPiyachokDTO(piaychok), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ItemListDTO<PiyachokDTO>> findPiyachokByPlaceId(int placeId, Integer page, Boolean old) {
        int itemsOnPage = 10;
        List<Piaychok> piaychoks = piyachokDAO.findAllByPlaceId(placeId).orElse(new ArrayList<>());
        if (piaychoks.size() != 0) {
            List<PiyachokDTO> piyachokDTOS = piaychoks.stream()
                    .map(PiyachokService::convertPiyachokToPiyachokDTO)
                    .collect(Collectors.toList());
            return itemListService.createResponseEntity(piyachokDTOS, itemsOnPage, page, old);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
