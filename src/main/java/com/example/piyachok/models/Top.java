package com.example.piyachok.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Top {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "places_tops",
            joinColumns = @JoinColumn(name = "top_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    @ToString.Exclude
    @JsonIgnore
private List<Place> places;

    public Top(String name, List<Place> places) {
        this.name = name;
        this.places = places;
    }

}
