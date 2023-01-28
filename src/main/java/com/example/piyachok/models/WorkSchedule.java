package com.example.piyachok.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WorkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String mondayStart;
    String mondayEnd;
    String tuesdayStart;
    String tuesdayEnd;
    String wednesdayStart;
    String wednesdayEnd;
    String thursdayStart;
    String thursdayEnd;
    String fridayStart;
    String fridayEnd;
    String saturdayStart;
    String saturdayEnd;
    String sundayStart;
    String sundayEnd;

    public WorkSchedule(String mondayStart, String mondayEnd, String tuesdayStart, String tuesdayEnd, String wednesdayStart, String wednesdayEnd, String thursdayStart, String thursdayEnd, String fridayStart, String fridayEnd, String saturdayStart, String saturdayEnd, String sundayStart, String sundayEnd) {
        this.mondayStart = mondayStart;
        this.mondayEnd = mondayEnd;
        this.tuesdayStart = tuesdayStart;
        this.tuesdayEnd = tuesdayEnd;
        this.wednesdayStart = wednesdayStart;
        this.wednesdayEnd = wednesdayEnd;
        this.thursdayStart = thursdayStart;
        this.thursdayEnd = thursdayEnd;
        this.fridayStart = fridayStart;
        this.fridayEnd = fridayEnd;
        this.saturdayStart = saturdayStart;
        this.saturdayEnd = saturdayEnd;
        this.sundayStart = sundayStart;
        this.sundayEnd = sundayEnd;
    }
}
