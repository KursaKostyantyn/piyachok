package com.example.piyachok.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "ratings")

public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private double rating;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "places_rating",
            joinColumns = @JoinColumn(name = "rating_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    @JsonBackReference(value = "place-rating")
    @ToString.Exclude
    private Place place;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_rating",
            joinColumns = @JoinColumn(name = "rating_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference(value = "user-rating")
    @ToString.Exclude
    private User user;


}
