package com.example.piyachok.models;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity(name = "refreshTokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false,unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expirationDate;
}
