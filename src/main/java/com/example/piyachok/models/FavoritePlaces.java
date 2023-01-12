package com.example.piyachok.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class FavoritePlaces {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;
    private int placeId;

    public FavoritePlaces(int userId, int placeId) {
        this.userId = userId;
        this.placeId = placeId;
    }


}
