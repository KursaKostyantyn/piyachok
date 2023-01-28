package com.example.piyachok.models.dto;

import com.example.piyachok.constants.Category;
import com.example.piyachok.models.Place;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;


import javax.persistence.NamedEntityGraph;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewsDTO {
    private int id;
    private Category category;
    private boolean isPaid;
    private LocalDate creationDate;
    private String text;
    private int placeId;
    private String placeName;
    private int userId;
    private String userLogin;
}
