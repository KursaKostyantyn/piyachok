package com.example.piyachok.controllers;

import com.example.piyachok.models.News;
import com.example.piyachok.models.dto.NewsDTO;
import com.example.piyachok.services.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/main/news")
public class NewsController {
    private NewsService newsService;

    @GetMapping("/allNews")
    public ResponseEntity<List<NewsDTO>> findAllNews (){
        return newsService.findAllNews();
    }

    @PostMapping("")
    public ResponseEntity<NewsDTO> saveNews (@RequestParam int placeId, @RequestBody News news){
        return newsService.saveNews(placeId,news);
    }
    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> findNewsByID(@PathVariable int id){
        return newsService.findNewsByID(id);
    }

    @GetMapping("/mainNews")
    public ResponseEntity<List<NewsDTO>> findMainNews(){
        return newsService.findMainNews();
    }


}
