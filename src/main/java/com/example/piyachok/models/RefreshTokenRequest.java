package com.example.piyachok.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RefreshTokenRequest {
    private String refreshToken;
}
