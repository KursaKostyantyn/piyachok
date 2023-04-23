package com.example.piyachok.services;

import com.example.piyachok.constants.Category;
import com.example.piyachok.constants.Gender;
import com.example.piyachok.constants.Role;
import com.example.piyachok.dao.FeatureDAO;
import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.dao.TypeDAO;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class AutoFillingService {

    private UserDAO userDAO;
    private PlaceDAO placeDAO;
    private TypeDAO typeDAO;

    private FeatureDAO featureDAO;
    private PasswordEncoder passwordEncoder;


    @Bean
    public void autoSaveEntities() {
        if (userDAO.findUserByLogin("admin").orElse(new User()).getLogin() == null
                && userDAO.findUserByLogin("admin1").orElse(new User()).getLogin() == null
                && userDAO.findUserByLogin("admin2").orElse(new User()).getLogin() == null
                && userDAO.findUserByLogin("admin3").orElse(new User()).getLogin() == null
                && userDAO.findUserByLogin("admin4").orElse(new User()).getLogin() == null
        ) {
            System.out.println("autoFilling started");
            List<Type> types = autoCreateListOfTypes();
            List<Feature> features=autoCreateListOfFeatures();
            for (int i = 0; i < 5; i++) {
                autoFillingEntity(i, types);
            }
            addTypesToPlaces(types);
            addFeaturesToPlaces();
        }
    }

    private List<News> autoCreateNewsList() {
        News news1 = new News(Category.MAIN, true, "Головні новини Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.");
        News news2 = new News(Category.MAIN, true, "Головні новини Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.");
        News news3 = new News(Category.MAIN, true, "Головні новини Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.");
        News news4 = new News(Category.EVENT, true, "Заходи новини Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.");
        News news5 = new News(Category.PROMOTION, true, "Промо новини Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.");
        List<News> newsList = new ArrayList<>();
        newsList.add(news1);
        newsList.add(news2);
        newsList.add(news3);
        newsList.add(news4);
        newsList.add(news5);
        return newsList;
    }

    private Piaychok autoCreateNewPiyachok(){
        Piaychok piaychok=new Piaychok();
        piaychok.setDate(new Date());
        piaychok.setAmountOfGuests((int)Math.round(Math.random()*5));
        piaychok.setDesireExpenses((int)Math.round(Math.random()*1000));
        piaychok.setMeetingDescription("some description of meeting");
        piaychok.setWriteToMe("forjava2022@gmail.com");
        return piaychok;
    }

    private void addTypesToPlaces(List<Type> types) {
        List<Place> places = placeDAO.findAll();
        int num = 0;
        for (Place place : places) {
            if (num == 5) {
                num = 0;
            }
            Type type = types.get(num);
            place.setTypes(new ArrayList<>());
            place.getTypes().add(types.get(num));
            placeDAO.save(place);
            typeDAO.save(type);
            num++;
        }
    }

    private void addFeaturesToPlaces(){
        List<Place> places=placeDAO.findAll();
        List<Feature> features=featureDAO.findAll();
        for (Feature feature:features){
            feature.setPlaces(new ArrayList<>());
        }
        int index=0;
        for (Place place:places){
            if (index==3){
                index=0;
            }
            Feature feature=features.get(index);
            place.setFeatures(new ArrayList<>());
            place.getFeatures().add(feature);
            feature.getPlaces().add(place);
            placeDAO.save(place);
            featureDAO.save(feature);
            index+=1;
        }
    }

    private Place autoCreatePlace(int number) {
        Address address = new Address("Київ", "Хрещатик", 17 + number);
        WorkSchedule workSchedule = new WorkSchedule("09:00", "18:00",
                "09:00", "18:00",
                "09:00", "18:00",
                "09:00", "18:00",
                "09:00", "18:00",
                "09:00", "18:00",
                "09:00", "18:00");
        Contact contact = new Contact("1234567890", "forjava2022@gmail.com");

        Place place = new Place("This is Pivbar" + number,

                address, workSchedule,
                true,
                "Description Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.",
                contact,
                (int) Math.round(Math.random() * 1000));
        place.setTypes(new ArrayList<>());
        place.setActivated(true);
        place.setPiaychoks(new ArrayList<>());
        place.getPiaychoks().add(autoCreateNewPiyachok());
        return place;
    }


    private List<Comment> autoCreateComments() {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Comment comment = new Comment();
            comment.setText("some comment №" + i + " " + "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.");
            comments.add(comment);
        }
        return comments;
    }

    private Rating autoCreatRating() {
        Rating rating = new Rating();
        rating.setRating(Math.round(Math.random() * (5 - 1) + 1));
        return rating;
    }

    private void autoFillingEntity(int number, List<Type> types) {
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
                    "forjava2022@gmail.com",
                    Role.ROLE_SUPERADMIN,
                    true, false,
                    placeList,
                    newsList);
            user.setComments(commentsList);
            user.setRatings(ratings);
            user.setFavoritePlaces(placeList);

            userDAO.save(user);

        } else {
            User user = new User("admin" + number,
                    "Mikola",
                    "Melnik",
                    passwordEncoder.encode("admin"),
                    LocalDate.of(1987, 12, 12),
                    "forjava2022@gmail.com",
                    Role.ROLE_ADMIN,
                    true, false,
                    placeList,
                    newsList);
            user.setComments(commentsList);
            user.setRatings(ratings);
            user.setFavoritePlaces(placeList);
            userDAO.save(user);
        }
    }

    private List<Type> autoCreateListOfTypes() {
        List<Type> types = new ArrayList<>();
        Type typeBar = new Type("Бар", new ArrayList<>());
        Type typePub = new Type("Паб", new ArrayList<>());
        Type typeRestaurant = new Type("Ресторан", new ArrayList<>());
        Type typeDisco = new Type("Дискотека", new ArrayList<>());
        Type typeCaffe = new Type("Кафе", new ArrayList<>());
        typeDAO.save(typeBar);
        typeDAO.save(typePub);
        typeDAO.save(typeRestaurant);
        typeDAO.save(typeDisco);
        typeDAO.save(typeCaffe);
        types.add(typeBar);
        types.add(typePub);
        types.add(typeRestaurant);
        types.add(typeDisco);
        types.add(typeCaffe);
        return types;
    }

    private List<Feature> autoCreateListOfFeatures() {
        List<Feature> features = new ArrayList<>();
        Feature wifi = new Feature("wi-fi", new ArrayList<>());
        Feature parking = new Feature("парковка", new ArrayList<>());
        Feature liveMusic = new Feature("жива музика", new ArrayList<>());
        featureDAO.save(wifi);
        featureDAO.save(parking);
        featureDAO.save(liveMusic);
        features.add(wifi);
        features.add(parking);
        features.add(liveMusic);
        return features;
    }


}
