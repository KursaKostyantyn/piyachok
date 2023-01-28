package com.example.piyachok.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class FavoritePlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "favoritePlaces_places",
            joinColumns = @JoinColumn(name = "favoritePlace_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    @JsonIgnore
    @ToString.Exclude
    private List<Place> places;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "favoritePlaces_users",
            joinColumns = @JoinColumn(name = "favoritePlace_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    @ToString.Exclude
    private List<User> users;

    public FavoritePlace(List<Place> places, List<User> users) {
        this.places = places;
        this.users = users;
    }
}
