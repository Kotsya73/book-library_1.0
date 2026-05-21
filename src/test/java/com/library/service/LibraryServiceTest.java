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
        library.borrowBook(book.getId(), reader.getId());

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
        library.borrowBook(book.getId(), reader1.getId());

        assertThrows(LibraryException.class, () -> library.borrowBook(book.getId(), reader2.getId()));
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
        library.borrowBook(book1.getId(), reader.getId());
        library.borrowBook(book2.getId(), reader.getId());
        library.borrowBook(book3.getId(), reader.getId());

        assertThrows(LibraryException.class, () -> library.borrowBook(book4.getId(), reader.getId()));
    }

    @Test
    void returnBook_shouldChangeStatus(){
        library.addBook("1984", "George Orwell");
        Book book = library.getBookByID(1L);
        library.registerReader("Adelina");
        Reader reader = library.getReaderByID(1L);
        library.borrowBook(book.getId(), reader.getId());
        library.returnBook(book.getId(), reader.getId());

        assertEquals(BookStatus.AVAILABLE, book.getStatus());
        assertNull(book.getBorrowedByID());
    }

    @Test
    void returnBook_shouldThrowExceptionStatus(){
        library.addBook("1984", "George Orwell");
        Book book1 = library.getBookByID(1L);
        library.addBook("On the Origin of Species", "Charles Darwin");
        Book book2 = library.getBookByID(2L);
        library.registerReader("Adelina");
        Reader reader = library.getReaderByID(1L);
        library.borrowBook(book1.getId(), reader.getId());

        assertThrows(LibraryException.class, () -> library.returnBook(book2.getId(), reader.getId()));
    }

    @Test
    void returnBook_shouldThrowExceptionReader(){
        library.addBook("1984", "George Orwell");
        Book book1 = library.getBookByID(1L);
        library.registerReader("Adelina");
        Reader reader1 = library.getReaderByID(1L);
        library.registerReader("Nick");
        Reader reader2 = library.getReaderByID(2L);

        library.borrowBook(book1.getId(), reader1.getId());

        assertThrows(LibraryException.class, () -> library.returnBook(book1.getId(), reader2.getId()));
    }

    @Test
    void findBooksByAuthor_shouldBeRightSize(){
        library.addBook("1984", "George Orwell");
        Book book1 = library.getBookByID(1L);
        library.addBook("Animal Farm", "George Orwell");
        Book book2 = library.getBookByID(2L);

        assertEquals(2, library.findBooksByAuthor("George Orwell").size());
    }

    @Test
    void findAvailableBooks_shouldBeRightSize(){
        library.addBook("1984", "George Orwell");
        Book book1 = library.getBookByID(1L);
        library.addBook("Animal Farm", "George Orwell");
        Book book2 = library.getBookByID(2L);
        library.addBook("On the Origin of Species", "Charles Darwin");
        Book book3 = library.getBookByID(3L);
        library.registerReader("Adelina");
        Reader reader1 = library.getReaderByID(1L);
        library.borrowBook(book3.getId(), reader1.getId());

        assertEquals(2, library.findAvailableBooks().size());
    }

    @Test
    void findBooksThatReaderHave_shouldBeRightSize(){
        library.addBook("1984", "George Orwell");
        Book book1 = library.getBookByID(1L);
        library.addBook("Animal Farm", "George Orwell");
        Book book2 = library.getBookByID(2L);
        library.addBook("On the Origin of Species", "Charles Darwin");
        Book book3 = library.getBookByID(3L);
        library.registerReader("Adelina");
        Reader reader1 = library.getReaderByID(1L);
        library.borrowBook(book1.getId(), reader1.getId());
        library.borrowBook(book2.getId(), reader1.getId());

        assertEquals(2, library.findBooksThatReaderHave(reader1.getId()).size());
    }

    @Test
    void findBookOrThrow_shouldThrowException(){
        library.registerReader("Adelina");
        Reader reader1 = library.getReaderByID(1L);

        assertThrows(LibraryException.class, () -> library.borrowBook(1L, reader1.getId()));
    }

    @Test
    void findReaderOrThrow_shouldThrowException(){
        library.addBook("1984", "George Orwell");
        Book book = library.getBookByID(1L);
        assertThrows(LibraryException.class, () -> library.borrowBook(book.getId(), 1L));
    }
}