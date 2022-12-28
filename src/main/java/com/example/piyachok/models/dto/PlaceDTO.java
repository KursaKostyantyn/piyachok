package com.example.piyachok.models.dto;

import com.example.piyachok.models.Address;
import com.example.piyachok.models.Contact;
import com.example.piyachok.models.User;
import lombok.*;


import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlaceDTO {

    private int id;
    private String name;
    private String photo;
    @ToString.Exclude
    private Address address;
    private String schedule;
    private boolean isActivated;
    private String description;
    @ToString.Exclude
    private Contact contacts;
    private int averageCheck;
    private LocalDate creationDate;
    private String type;
    private User user;
}
