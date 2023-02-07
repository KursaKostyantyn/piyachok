package com.example.piyachok.services;

import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.NewsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ItemListService {

    public <T> ResponseEntity<ItemListDTO<T>> createResponseEntity(List<T> itemList, Integer itemsOnPage, Integer page, Boolean old) {
        page = checkValueOfPage(page);
        old = checkValueOfOld(old);
        if (itemList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ItemListDTO<T> itemListDTO = createItemList(itemList, itemsOnPage, page, old);
        if (itemListDTO.getItems() != null) {
            return new ResponseEntity<>(itemListDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    public <T> ItemListDTO<T> createItemList(List<T> itemList, int itemsOnPage, int page, Boolean old) {
        ItemListDTO<T> itemListDTO = new ItemListDTO<>();
        if (old) {
            Collections.reverse(itemList);
        }
        int amountOfPages = itemList.size() / itemsOnPage;
        int pageNumber = page;
        if (itemList.size() % itemsOnPage != 0) {
            amountOfPages += 1;
        }

        if (page > amountOfPages) {
            pageNumber = amountOfPages;
        }
        if (page <= 0) {
            pageNumber = 1;
        }

        int startIndex = (pageNumber - 1) * itemsOnPage;

        if (itemsOnPage > itemList.size()) {
            startIndex = 0;
        }
        int endIndex = startIndex + itemsOnPage;
        if (endIndex > itemList.size()) {
            endIndex = itemList.size();
        }

        itemListDTO.setItems(itemList.subList(startIndex, endIndex));

        itemListDTO.setAmountOfPages(amountOfPages);
        itemListDTO.setAmountOfItems(itemList.size());
        if (pageNumber - 1 != 0) {
            itemListDTO.setPreviousPage(pageNumber - 1);
        }
        if (page + 1 <= amountOfPages) {
            itemListDTO.setNextPage(pageNumber + 1);
        }

        itemListDTO.setCurrentPage(pageNumber);
        return itemListDTO;
    }

    private Integer checkValueOfPage(Integer page) {
        if (page == null) {
            page = 1;
        }
        return page;
    }

    private Boolean checkValueOfOld(Boolean old) {
        if (old == null) {
            old = false;
        }
        return old;
    }

}
