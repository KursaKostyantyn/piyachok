package com.example.piyachok.models.dto;

import com.example.piyachok.models.Place;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddressDTO {

    private int id;
    private String city;
    private String street;
    private int number;
    @ToString.Exclude
    private Place place;
}
