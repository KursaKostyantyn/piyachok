package com.example.piyachok.models.dto;

import com.example.piyachok.models.*;
import lombok.*;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
    private List<TypeDTO> types;
    private int  userId;
    private double averageRating;
    private List<NewsDTO> news;
    private Set<String> photos;
    private List<FeatureDTO> features;

}
