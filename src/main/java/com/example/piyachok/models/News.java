package com.example.piyachok.models;

import com.example.piyachok.constants.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Category category;
    private boolean isPaid;
    private LocalDate creationDate = LocalDate.now();
    private String text;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "places_news",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    @JsonBackReference(value = "place-news")
    @ToString.Exclude
    private Place place;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_news",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference(value = "user-news")
    @ToString.Exclude
    private User user;

    public News(Category category, boolean isPaid, String text) {
        this.category = category;
        this.isPaid = isPaid;
        this.text = text;
    }
}
