package com.example.piyachok.models.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemListDTO<T>{
    private int previousPage;
    private int nextPage;
    private int amountOfPages;
    private int amountOfItems;
    private int currentPage;
    private List<T> items;
}
