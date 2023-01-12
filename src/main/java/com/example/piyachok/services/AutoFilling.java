package com.example.piyachok.services;

import com.example.piyachok.constants.Category;
import com.example.piyachok.constants.Role;
import com.example.piyachok.dao.FavoritePlacesDAO;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AutoFilling {

    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;
    private FavoritePlacesDAO favoritePlacesDAO;

    @Bean
    public void autoSaveEntities() {
        if (userDAO.findUserByLogin("admin") == null
                && userDAO.findUserByLogin("admin1") == null
                && userDAO.findUserByLogin("admin2") == null
                && userDAO.findUserByLogin("admin3") == null
                && userDAO.findUserByLogin("admin4") == null
        ) {
            System.out.println("autoFilling started");
            for (int i = 0; i < 5; i++) {
                autoFillingEntity(i);
            }

        }
    }

    public List<News> autoCreateNewsList() {
        News news1 = new News(Category.MAIN, true, "Main news Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.");
        News news2 = new News(Category.MAIN, true, "Main news Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.");
        News news3 = new News(Category.MAIN, true, "Main news Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.");
        News news4 = new News(Category.EVENT, true, "Event news Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.");
        News news5 = new News(Category.PROMOTION, true, "Promotion news Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.");
        List<News> newsList = new ArrayList<>();
        newsList.add(news1);
        newsList.add(news2);
        newsList.add(news3);
        newsList.add(news4);
        newsList.add(news5);
        return newsList;
    }

    public Place autoCreatePlace(int number) {
        Address address = new Address("Kiev", "Hreshatik", 17 + number);
        WorkSchedule workSchedule = new WorkSchedule("09:00-18:00", "09:00-18:00", "09:00-18:00", "09:00-18:00", "09:00-18:00", "09:00-18:00", "09:00-18:00");
        Contact contact = new Contact("1234567890", "test@test.com");

        Place place = new Place("This is Pivbar" + number,
                "photo.png",
                address, workSchedule,
                true,
                "Description Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.",
                contact,
                580,
                "bar");
        return place;
    }

    public void autoFillingFavoritePlaces(int userId, int placeId) {
        favoritePlacesDAO.save(new FavoritePlaces(userId, placeId));
    }

    public List<Comment> autoCreateComments() {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Comment comment = new Comment();
            comment.setText("some comment №" + i + " " + "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.");
            comments.add(comment);
        }
        return comments;
    }

    public Rating autoCreatRating() {
        Rating rating = new Rating();
        rating.setRating(Math.random() * 5);
        return rating;
    }

    public void autoFillingEntity(int number) {
        List<Place> placeList = new ArrayList<>();
        List<News> newsList = new ArrayList<>();
        List<Comment> commentsList = new ArrayList<>();
        List<Rating> ratings = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Place place = autoCreatePlace(i);
            List<News> news = autoCreateNewsList();
            List<Comment> comments = autoCreateComments();
            Rating rating = autoCreatRating();
            List<Rating> ratingList = new ArrayList<>();
            ratingList.add(rating);
            place.setNews(news);
            place.setComments(comments);
            place.setRatings(ratingList);
            newsList.addAll(news);
            commentsList.addAll(comments);
            placeList.add(place);
            ratings.add(rating);
        }
        if (number == 0) {
            User user = new User("admin",
                    "Mikola",
                    "Melnik",
                    passwordEncoder.encode("admin"),
                    LocalDate.of(1987, 12, 12),
                    "admin@test.com",
                    Role.ROLE_SUPERADMIN,
                    true, false,
                    placeList,
                    newsList);
            user.setComments(commentsList);
            user.setRatings(ratings);
            userDAO.save(user);
            for (Place place : placeList) {

                autoFillingFavoritePlaces(user.getId(), place.getId());
            }

        } else {
            User user = new User("admin" + number,
                    "Mikola",
                    "Melnik",
                    passwordEncoder.encode("admin"),
                    LocalDate.of(1987, 12, 12),
                    "admin@test.com",
                    Role.ROLE_ADMIN,
                    true, false,
                    placeList,
                    newsList);
            user.setComments(commentsList);
            user.setRatings(ratings);
            userDAO.save(user);
            for (Place place : placeList) {
                autoFillingFavoritePlaces(user.getId(), place.getId());
            }
        }
    }
}
