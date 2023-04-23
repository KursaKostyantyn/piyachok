package com.example.piyachok.controllers;

import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.PiyachokDTO;
import com.example.piyachok.services.PiyachokService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("piyachoks")
public class PiyachokController {
    private PiyachokService piyachokService;

    @GetMapping("")
    public ResponseEntity<ItemListDTO<PiyachokDTO>> findAllPiyachok(@RequestParam Integer page, @RequestParam Boolean old) {
        return piyachokService.findAllPiyachok(page, old);
    }

    @PostMapping("")
    public ResponseEntity<PiyachokDTO> savePiyachok(@RequestBody PiyachokDTO piyachokDTO) {
        System.out.println(piyachokDTO);
        return piyachokService.savePiyachok(piyachokDTO);
    }

    @DeleteMapping("/{piyachokId}")
    public ResponseEntity<HttpStatus> deletePiyachokById(@PathVariable int piyachokId) {
        return piyachokService.deletePiyachokById(piyachokId);
    }

    @GetMapping("/{piyachokId}")
    public ResponseEntity<PiyachokDTO> findPiyachokById(@PathVariable int piyachokId) {
        return piyachokService.findPiyachokById(piyachokId);
    }

    @GetMapping("/places/{placeId}")
    public ResponseEntity<ItemListDTO<PiyachokDTO>> findPiyachokByPlaceId(@PathVariable int placeId,
                                                                          @RequestParam(required = false) Integer page,
                                                                          @RequestParam(required = false) Boolean old) {
        return piyachokService.findPiyachokByPlaceId(placeId, page, old);
    }

}
