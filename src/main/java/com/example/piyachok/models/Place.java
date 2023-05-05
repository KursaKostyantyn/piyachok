package com.example.piyachok.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
//    private String photo;

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
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "places_types",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )

    @ToString.Exclude
    @Fetch(FetchMode.SELECT)
    private List<Type> types;

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "places_features",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    @ToString.Exclude
    @Fetch(FetchMode.SELECT)
    private List<Feature> features;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_places",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference(value = "user-place")
    @ToString.Exclude
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "place-news")
    @JoinTable(
            name = "places_news",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "news_id")
    )
    @ToString.Exclude
    @Fetch(FetchMode.SELECT)
    private List<News> news;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "places_comments",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    @JsonManagedReference(value = "place-comment")
    @ToString.Exclude
    @Fetch(FetchMode.SELECT)
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "places_rating",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "rating_id")
    )
    @JsonManagedReference(value = "place-rating")
    @ToString.Exclude
    @Fetch(FetchMode.SELECT)
    private List<Rating> ratings=new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "places_piyachok",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "piyachok_id")
    )
    @ToString.Exclude
    @JsonManagedReference(value = "place-piyachok")
    @Fetch(FetchMode.SELECT)
    private List<Piaychok> piaychoks=new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "places_tops",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "top_id")
    )
    @ToString.Exclude
    @Fetch(FetchMode.SELECT)
    private List<Top> tops;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> photos = new HashSet<>();




    public Place(String name, Address address, WorkSchedule workSchedule, boolean isActivated, String description, Contact contacts, int averageCheck, List<Type> types, List<News> news) {
        this.name = name;

        this.address = address;
        this.workSchedule = workSchedule;
        this.activated = isActivated;
        this.description = description;
        this.contacts = contacts;
        this.averageCheck = averageCheck;
        this.types = types;
        this.news = news;
    }

    public Place(String name, Address address, WorkSchedule workSchedule, boolean isActivated, String description, Contact contacts, int averageCheck) {
        this.name = name;
        this.address = address;
        this.workSchedule = workSchedule;
        this.activated = isActivated;
        this.description = description;
        this.contacts = contacts;
        this.averageCheck = averageCheck;

    }
}
