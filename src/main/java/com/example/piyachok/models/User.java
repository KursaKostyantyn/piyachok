package com.example.piyachok.models;

import com.example.piyachok.constants.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String login;

    private String firstName;
    private String lastName;
    private String password;
    private LocalDate birthDate;
    private String email;
    private Role role = Role.ROLE_ADMIN;
    private boolean isActivated;
    private boolean isBlocked;
    private LocalDate creationDate =LocalDate.now();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinTable(
            name = "users_places",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    @ToString.Exclude
    private List<Place> places;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-news")
    @JoinTable(
            name = "users_news",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "news_id")
    )
    @ToString.Exclude
    private List<News> news;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_comments",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    @JsonManagedReference(value = "user-comment")
    @ToString.Exclude
    private List<Comment> comments;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_rating",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "rating_id")
    )
    @JsonManagedReference(value = "user-rating")
    @ToString.Exclude
    private List<Rating> ratings;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "refreshToken_id", referencedColumnName = "id")
    private RefreshToken refreshToken;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "favoritePlaces_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "favoritePlace_id")
    )
    @ToString.Exclude
    private List<FavoritePlace> favoritePlacesEntity;

    @OneToMany
    private List<Place> favoritePlaces;





    public User(String login, String firstName, String lastName, String password, LocalDate birthDate, String email, Role role, boolean isActivated, boolean isBlocked, List<Place> places, List<News> news) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.birthDate = birthDate;
        this.email = email;
        this.role = role;
        this.isActivated = isActivated;
        this.isBlocked = isBlocked;
        this.places = places;
        this.news=news;
    }
}
