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
    private int placeId;
    private String userLogin;
    private String placeName;

    public CommentDTO(String text) {
        this.text = text;
    }
}
