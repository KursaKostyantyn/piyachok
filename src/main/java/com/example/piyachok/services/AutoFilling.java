package com.example.piyachok.services;

import com.example.piyachok.constants.Category;
import com.example.piyachok.constants.Role;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AutoFilling {

    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

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

    public Place autoCreatePlace(int number) {
        Address address = new Address("Kiev", "Hreshatik", 17 + number);
        WorkSchedule workSchedule = new WorkSchedule("09:00-18:00", "09:00-18:00", "09:00-18:00", "09:00-18:00", "09:00-18:00", "09:00-18:00", "09:00-18:00");
        Contact contact = new Contact("1234567890", "test@test.com");
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
        Place place = new Place("This is Pivbar" + number,
                "phot.png",
                address, workSchedule,
                true,
                "Description Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia ex nunc, sit amet euismod metus aliquet et. Sed sed.",
                contact,
                580,
                "bar",
                newsList);
        return place;
    }

    public void autoFillingEntity(int number) {
        List<Place> placeList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Place place = autoCreatePlace(i);
            placeList.add(place);
        }
        if (number == 0) {
            User user = new User("admin",
                    "Mikola",
                    "Melnik",
                    passwordEncoder.encode("admin"),
                    LocalDate.of(1987, 12, 12),
                    "admin@test.com",
                    Role.ROLE_ADMIN,
                    true, false,
                    placeList);
            userDAO.save(user);
        } else {
            User user = new User("admin" + number,
                    "Mikola",
                    "Melnik",
                    passwordEncoder.encode("admin"),
                    LocalDate.of(1987, 12, 12),
                    "admin@test.com",
                    Role.ROLE_ADMIN,
                    true, false,
                    placeList);
            userDAO.save(user);
        }
    }
}
