package com.example.piyachok.models.dto;

import com.example.piyachok.models.*;
import lombok.*;


import java.time.LocalDate;
import java.util.List;

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
    private WorkSchedule workSchedule;
    private boolean isActivated;
    private String description;
    @ToString.Exclude
    private Contact contacts;
    private int averageCheck;
    private LocalDate creationDate;
    private List<Type> types;
    private int  userId;
    private List<News> news;

}
