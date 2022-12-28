package com.example.piyachok.models.dto;

import com.example.piyachok.constants.Role;
import com.example.piyachok.models.Place;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UserDTO {

    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private Role role;
    private boolean isActivated;
    private boolean isBlocked;
    private List<Place> places;
}
