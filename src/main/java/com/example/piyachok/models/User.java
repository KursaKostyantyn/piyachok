package com.example.piyachok.models;

import com.example.piyachok.constants.Role;
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
}
