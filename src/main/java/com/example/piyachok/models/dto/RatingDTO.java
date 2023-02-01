package com.example.piyachok.models.dto;

import com.example.piyachok.models.Place;
import com.example.piyachok.models.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RatingDTO {
    private int id;
    private double rating;
    private int placeId;
    private String placeName;
    private String userLogin;
}
