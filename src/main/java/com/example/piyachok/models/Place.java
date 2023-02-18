package com.example.piyachok.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Entity(name = "places")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String photo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "passport_id", referencedColumnName = "id")
    @ToString.Exclude
    private Address address;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "workSchedule_id", referencedColumnName = "id")
    private WorkSchedule workSchedule;


    private boolean activated = false;
    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    @ToString.Exclude
    private Contact contacts;

    private int averageCheck;
    private LocalDate creationDate = LocalDate.now();
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "places_types",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )

    @ToString.Exclude
    private List<Type> types;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_places",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference (value = "user-place")
    @ToString.Exclude
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "place-news")
    @JoinTable(
            name = "places_news",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "news_id")
    )
    @ToString.Exclude
    private List<News> news;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "places_comments",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    @JsonManagedReference(value = "place-comment")
    @ToString.Exclude
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "places_rating",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "rating_id")
    )
    @JsonManagedReference(value = "place-rating")
    @ToString.Exclude
    private List<Rating> ratings;


    public Place(String name, String photo, Address address, WorkSchedule workSchedule, boolean isActivated, String description, Contact contacts, int averageCheck, List<Type> types, List<News> news) {
        this.name = name;
        this.photo = photo;
        this.address = address;
        this.workSchedule = workSchedule;
        this.activated = isActivated;
        this.description = description;
        this.contacts = contacts;
        this.averageCheck = averageCheck;
        this.types = types;
        this.news = news;
    }

    public Place(String name, String photo, Address address, WorkSchedule workSchedule, boolean isActivated, String description, Contact contacts, int averageCheck) {
        this.name = name;
        this.photo = photo;
        this.address = address;
        this.workSchedule = workSchedule;
        this.activated = isActivated;
        this.description = description;
        this.contacts = contacts;
        this.averageCheck = averageCheck;

    }
}
