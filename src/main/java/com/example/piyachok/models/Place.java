package com.example.piyachok.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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

    private String schedule;
    private boolean isActivated;
    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    @ToString.Exclude
    private Contact contacts;

    private int averageCheck;
    private LocalDate creationDate;
    private String type;

    @ManyToOne (cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_places",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference
    @ToString.Exclude
    private User user;

}
