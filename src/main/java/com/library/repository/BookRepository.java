package com.library.repository;

import com.library.model.Book;

import java.util.*;

public class BookRepository {
    private final AutoIncrementMap<Book> bookStorage = new AutoIncrementMap<>();

    public void saveBook(Book book) {
        bookStorage.put(book);
    }

    public Book getBookById(Long bookId) {
        return bookStorage.get(bookId);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(bookStorage.getAll());
    }
}
