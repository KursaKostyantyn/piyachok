package com.example.piyachok.models.dto;

import com.example.piyachok.constants.Gender;
import com.example.piyachok.models.Place;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PiyachokDTO {
    private int id;
    private String date;
    private String meetingDescription;
    private String writeToMe;
    private int amountOfGuests;
    private int desireExpenses;
    private int placeId;
}
