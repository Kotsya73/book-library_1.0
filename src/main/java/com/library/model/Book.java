package com.library.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Identifiable {
    private Long id;
    private String title;
    private String author;
    private BookStatus status;
    private Long borrowedByID;
}
