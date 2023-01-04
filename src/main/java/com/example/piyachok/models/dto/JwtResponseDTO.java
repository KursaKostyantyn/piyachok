package com.example.piyachok.models.dto;

import com.example.piyachok.models.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class JwtResponseDTO {
    private String jwtToken;
    private String refreshToken;
    private User user;
}
