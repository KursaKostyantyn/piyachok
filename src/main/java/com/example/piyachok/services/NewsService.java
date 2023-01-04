package com.example.piyachok.services;

import com.example.piyachok.constants.Category;
import com.example.piyachok.dao.NewsDAO;
import com.example.piyachok.models.News;
import com.example.piyachok.models.dto.NewsDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class NewsService {
    private NewsDAO newsDAO;
    private PlaceService placeService;

    public NewsDTO convertNewsToNewsDTO(News news) {
        return new NewsDTO(
                news.getId(),
                news.getCategory(),
                news.isPaid(),
                news.getCreationDate(),
                news.getText(),
                news.getPlace().getId()
        );
    }

    public ResponseEntity<List<NewsDTO>> findAllNews() {

        List<NewsDTO> newsDTOList = newsDAO.findAll()
                .stream()
                .map(this::convertNewsToNewsDTO)
                .collect(Collectors.toList());
        if (newsDTOList.size() != 0) {
            return new ResponseEntity<>(newsDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<NewsDTO> saveNews(int placeId, News news) {

        if (placeService.addPlaceNews(placeId, news)) {
            newsDAO.save(news);
            System.out.println("news" + news.getPlace());
            return new ResponseEntity<>(convertNewsToNewsDTO(news), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<NewsDTO> findNewsByID (int id){
        News news = newsDAO.findById(id).orElse(new News());
        if (news.getText()!=null){
            return new ResponseEntity<>(convertNewsToNewsDTO(news),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<NewsDTO>> findMainNews(){
        List<NewsDTO> newsDTOList = newsDAO.findAll()
                .stream()
                .filter(news -> news.getCategory()== Category.MAIN)
                .map(this::convertNewsToNewsDTO)
                .collect(Collectors.toList());
        if (newsDTOList.size()!=0){
            return new ResponseEntity<>(newsDTOList,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
