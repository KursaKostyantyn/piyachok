package com.example.piyachok.models;

import com.example.piyachok.constants.Gender;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.security.access.method.P;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Piaychok {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date date;
    private String meetingDescription;
    private String writeToMe;
    private int amountOfGuests;
    private int desireExpenses;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "places_piyachok",
            joinColumns = @JoinColumn(name = "piyachok_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    @ToString.Exclude
    @JsonBackReference(value = "place-piyachok")
    private Place place;





}
