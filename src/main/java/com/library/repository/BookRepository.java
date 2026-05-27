package com.library.repository;

import com.library.model.Book;

import java.util.*;

public class BookRepository {
    private final Map<Long, Book> bookStorage = new HashMap<>();

    public void saveBook(Book book) {
        bookStorage.put(book.getId(), book);
    }

    public Book getBookById(Long bookId) {
        return bookStorage.get(bookId);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(bookStorage.values());
    }
}
