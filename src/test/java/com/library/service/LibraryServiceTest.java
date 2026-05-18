package com.library.service;

import com.library.model.Book;
import com.library.model.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceTest {

    private LibraryService library;

    @BeforeEach
    void setUp() {
        library = new LibraryService();
    }

    @Test
    void addBook_shouldAddBookToStorage(){
        library.addBook("1984", "George Orwell");
        Book book = library.getBookByID(1L);
        assertNotNull(book);
        assertEquals("1984", book.getTitle());
        assertEquals("George Orwell", book.getAuthor());
    }

    @Test
    void registerReader_shouldAddReaderToStorage(){
        library.registerReader("Adelina");
        Reader reader = library.getReaderByID(1L);
        assertNotNull(reader);
        assertEquals("Adelina", reader.getName());
    }
}