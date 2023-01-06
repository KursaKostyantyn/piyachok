package com.example.piyachok.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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


    private boolean isActivated;
    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    @ToString.Exclude
    private Contact contacts;

    private int averageCheck;
    private LocalDate creationDate =LocalDate.now();
    private String type;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_places",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference
    @ToString.Exclude
    private User user;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinTable(
            name = "places_news",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "news_id")
    )
    @ToString.Exclude
    private List<News> news;

    public Place(String name, String photo, Address address, WorkSchedule workSchedule, boolean isActivated, String description, Contact contacts, int averageCheck, String type, List<News> news) {
        this.name = name;
        this.photo = photo;
        this.address = address;
        this.workSchedule = workSchedule;
        this.isActivated = isActivated;
        this.description = description;
        this.contacts = contacts;
        this.averageCheck = averageCheck;
        this.type = type;
        this.news = news;
    }
}
