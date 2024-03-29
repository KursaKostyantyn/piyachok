package com.example.piyachok.services;

import com.example.piyachok.constants.Category;
import com.example.piyachok.constants.Role;
import com.example.piyachok.dao.NewsDAO;
import com.example.piyachok.models.News;
import com.example.piyachok.models.dto.ItemListDTO;
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
    private UserService userService;

    private ItemListService itemListService;

    public static NewsDTO convertNewsToNewsDTO(News news) {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(news.getId());
        newsDTO.setCategory(news.getCategory());
        newsDTO.setPaid(news.isPaid());
        newsDTO.setCreationDate(news.getCreationDate());
        newsDTO.setText(news.getText());
        newsDTO.setPlaceId(news.getPlace().getId());
        newsDTO.setUserId(news.getUser().getId());
        return newsDTO;
    }

    public News convertNewsDTOToNews(NewsDTO newsDTO) {
        News news = new News();
        news.setPaid(newsDTO.isPaid());
        news.setText(newsDTO.getText());
        news.setCategory(newsDTO.getCategory());
        return news;
    }

    public ResponseEntity<ItemListDTO<NewsDTO>> findAllNews(Integer page, Boolean old) {
        int itemsOnPage = 10;

        List<NewsDTO> newsDTOList = newsDAO.findAll()
                .stream()
                .map(NewsService::convertNewsToNewsDTO)
                .collect(Collectors.toList());

        return itemListService.createResponseEntity(newsDTOList, itemsOnPage, page, old);
    }

    public ResponseEntity<NewsDTO> saveNews(int placeId, String login, NewsDTO newsDTO) {
        if (newsDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        News news = convertNewsDTOToNews(newsDTO);

        if (!(SecurityService.authorizedUserHasRole(Role.ROLE_SUPERADMIN.getUserRole()) |
                SecurityService.getLoginAuthorizedUser().equals(login))
        ) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (placeService.addNewsToPlace(placeId, news) &
                userService.addNewsToUser(login, news)) {
            newsDAO.save(news);
            return new ResponseEntity<>(convertNewsToNewsDTO(news), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<NewsDTO> findNewsById(int newsId) {
        News news = newsDAO.findById(newsId).orElse(new News());
        if (news.getText() != null) {
            return new ResponseEntity<>(convertNewsToNewsDTO(news), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ItemListDTO<NewsDTO>> findMainNews(Integer page, Boolean old) {
        int itemsOnPage = 10;
        List<NewsDTO> newsDTOList = newsDAO.findAll()
                .stream()
                .filter(news -> news.getCategory() == Category.MAIN)
                .map(NewsService::convertNewsToNewsDTO)
                .collect(Collectors.toList());
        return itemListService.createResponseEntity(newsDTOList, itemsOnPage, page, old);
    }

    public ResponseEntity<ItemListDTO<NewsDTO>> findNewsByUserId(Integer page, Boolean old, int userId) {
        int itemsOnPage = 10;
        if (userId == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<NewsDTO> newsDTOList = newsDAO.findNewsByUserId(userId)
                .stream()
                .map(NewsService::convertNewsToNewsDTO)
                .collect(Collectors.toList());
        return itemListService.createResponseEntity(newsDTOList, itemsOnPage, page, old);
    }

    public ResponseEntity<NewsDTO> updateNewsById(int id, NewsDTO news) {
        News newsById = newsDAO.findNewsById(id).orElse(new News());
        if (!(SecurityService.authorizedUserHasRole(Role.ROLE_SUPERADMIN.getUserRole()) |
                SecurityService.getLoginAuthorizedUser().equals(newsById.getUser().getLogin()))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (newsById.getId() != 0) {
            newsById.setCategory(news.getCategory());
            newsById.setText(news.getText());
            newsById.setPaid(news.isPaid());
            newsDAO.save(newsById);
            return new ResponseEntity<>(convertNewsToNewsDTO(newsById), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<HttpStatus> deleteNewsById(int id) {
        News newsById = newsDAO.findNewsById(id).orElse(new News());
        if (!(SecurityService.authorizedUserHasRole(Role.ROLE_SUPERADMIN.getUserRole())|
                SecurityService.getLoginAuthorizedUser().equals(newsById.getUser().getLogin()))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (newsById.getId() != 0) {
            newsDAO.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
