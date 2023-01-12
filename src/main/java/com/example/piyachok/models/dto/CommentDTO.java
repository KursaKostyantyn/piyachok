package com.example.piyachok.models.dto;

import com.example.piyachok.models.Place;
import com.example.piyachok.models.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CommentDTO {
    private int id;
    private String text;
    private Place place;
    private User user;

    public CommentDTO(String text) {
        this.text = text;
    }
}
