package com.example.piyachok.controllers;

import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.NewsDTO;
import com.example.piyachok.services.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("news")
public class NewsController {
    private NewsService newsService;

    @GetMapping("/allNews")
    public ResponseEntity<ItemListDTO<NewsDTO>> findAllNews (@RequestParam(required = false) Integer page, @RequestParam(required = false) Boolean old){
        return newsService.findAllNews(page,old);
    }

    @PostMapping("")
    public ResponseEntity<NewsDTO> saveNews (@RequestParam int placeId, @RequestParam String login, @RequestBody NewsDTO newsDTO){
        return newsService.saveNews(placeId,login, newsDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> findNewsByID(@PathVariable int id){
        return newsService.findNewsById(id);
    }

    @GetMapping("/mainNews")
    public ResponseEntity<ItemListDTO<NewsDTO>> findMainNews(@RequestParam(required = false) Integer page, @RequestParam(required = false) Boolean old){
        return newsService.findMainNews(page,old);
    }

    @GetMapping("")
    public ResponseEntity<ItemListDTO<NewsDTO>> findNewsByUserId(@RequestParam(required = false) Integer page, @RequestParam(required = false) Boolean old,@RequestParam int userId){
        return newsService.findNewsByUserId(page,old,userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsDTO> updateNewsById(@PathVariable int id,@RequestBody NewsDTO news){
        return newsService.updateNewsById(id, news);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteNewsById(@PathVariable int id){
      return newsService.deleteNewsById(id);
    }


}
