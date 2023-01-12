package com.example.piyachok.models.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class JwtResponseDTO {
    private String accessToken;
    private String refreshToken;
    private UserDTO userDTO;
}
