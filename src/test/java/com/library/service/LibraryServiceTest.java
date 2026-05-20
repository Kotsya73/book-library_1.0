package com.library.service;

import com.library.exception.LibraryException;
import com.library.model.Book;
import com.library.model.BookStatus;
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
    void addBook_shouldChangeCounter(){
        library.addBook("1984", "George Orwell");
        assertEquals(2, library.getBookCounterForID());
    }

    @Test
    void registerReader_shouldAddReaderToStorage(){
        library.registerReader("Adelina");
        Reader reader = library.getReaderByID(1L);
        assertNotNull(reader);
        assertEquals("Adelina", reader.getName());
    }

    @Test
    void registerReader_shouldChangeCounter(){
        library.registerReader("Adelina");
        assertEquals(2, library.getReaderCounterForID());
    }

    @Test
    void borrowBook_shouldChangeStatus(){
        library.addBook("1984", "George Orwell");
        Book book = library.getBookByID(1L);
        library.registerReader("Adelina");
        Reader reader = library.getReaderByID(1L);
        library.borrowBook(book.getId(), reader.getID());

        assertEquals(BookStatus.BORROWED, book.getStatus());
    }

    @Test
    void borrowBook_shouldThrowExceptionBorrowed(){
        library.addBook("1984", "George Orwell");
        Book book = library.getBookByID(1L);
        library.registerReader("Adelina");
        library.registerReader("Nick");
        Reader reader1 = library.getReaderByID(1L);
        Reader reader2 = library.getReaderByID(2L);
        library.borrowBook(book.getId(), reader1.getID());

        assertThrows(LibraryException.class, () -> library.borrowBook(book.getId(), reader2.getID()));
    }

    @Test
    void borrowBook_shouldThrowExceptionLimit(){
        library.addBook("1984", "George Orwell");
        Book book1 = library.getBookByID(1L);
        library.addBook("On the Origin of Species", "Charles Darwin");
        Book book2 = library.getBookByID(2L);
        library.addBook("The Great Gatsby", "F Scott Fitzgerald");
        Book book3 = library.getBookByID(3L);
        library.addBook("Lord Of The Rings Trilogy", "J.R.R. Tolkien");
        Book book4 = library.getBookByID(4L);

        library.registerReader("Adelina");
        Reader reader = library.getReaderByID(1L);
        library.borrowBook(book1.getId(), reader.getID());
        library.borrowBook(book2.getId(), reader.getID());
        library.borrowBook(book3.getId(), reader.getID());

        assertThrows(LibraryException.class, () -> library.borrowBook(book4.getId(), reader.getID()));
    }
}