package com.example.piyachok.models.dto;

import com.example.piyachok.models.Place;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ContactDTO {

    private int id;
    private String phone;
    private String email;
    @ToString.Exclude
    private Place place;
}
