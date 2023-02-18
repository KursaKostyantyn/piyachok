package com.example.piyachok.models.dto;

import com.example.piyachok.constants.Role;
import com.example.piyachok.models.News;
import com.example.piyachok.models.Place;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UserDTO {

    private int id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private Role role;
    private boolean isActivated;
    private boolean isBlocked;
    private LocalDate creationDate;
    private List<Place> places;
    private List<News> news;
    private String photo;

    public UserDTO(int id, String login, String firstName, String lastName, LocalDate birthDate, String email, Role role, boolean isActivated, boolean isBlocked, LocalDate creationDate, List<Place> places, List<News> news) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.role = role;
        this.isActivated = isActivated;
        this.isBlocked = isBlocked;
        this.creationDate = creationDate;
        this.places = places;
        this.news = news;
    }

}
