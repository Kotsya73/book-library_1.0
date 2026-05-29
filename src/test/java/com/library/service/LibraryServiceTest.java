package com.library.service;

import com.library.exception.LibraryException;
import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Reader;
import com.library.repository.BookRepository;
import com.library.repository.ReaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


// replace assertions with assertThat() -> add assertJ to maven
class LibraryServiceTest {

    private LibraryService library;

    @BeforeEach
    void setUp() {
        library = new LibraryService(new BookRepository(), new ReaderRepository());
    }

    @Test
    void addBook_shouldThrowExceptionIfNoTitle() {
        assertThatThrownBy(() -> library.addBook("", "George Orwell")).isInstanceOf(LibraryException.class);
        assertThatThrownBy(() -> library.addBook("  ", "Charles Darwin")).isInstanceOf(LibraryException.class);
    }

    @Test
    void addBook_shouldThrowExceptionIfNoAuthor() {
        assertThatThrownBy(() -> library.addBook("1984", "")).isInstanceOf(LibraryException.class);
        assertThatThrownBy(() -> library.addBook("On the Origin of Species", "  ")).isInstanceOf(LibraryException.class);
    }

    @Test
    void addBook_shouldAddBookToStorage() {
        library.addBook("1984", "George Orwell");
        Book book = library.getBookByID(1L);
        assertThat(book).isNotNull();
        assertThat(book.getTitle()).isEqualTo("1984");
        assertThat(book.getAuthor()).isEqualTo("George Orwell");
    }

    @Test
    void addBook_shouldChangeCounter() {
        library.addBook("1984", "George Orwell");
        Book book1 = library.getBookByID(1L);
        library.addBook("On the Origin of Species", "Charles Darwin");
        Book book2 = library.getBookByID(2L);

        assertThat(book2.getId()).isEqualTo(2L);
    }

    @Test
    void registerReader_shouldThrowExceptionNoName() {
        assertThatThrownBy(() -> library.registerReader("")).isInstanceOf(LibraryException.class);
        assertThatThrownBy(() -> library.registerReader(" ")).isInstanceOf(LibraryException.class);
    }

    @Test
    void registerReader_shouldAddReaderToStorage() {
        library.registerReader("Adelina");
        Reader reader = library.getReaderByID(1L);
        assertThat(reader).isNotNull();
        assertThat(reader.getName()).isEqualTo("Adelina");
    }

    @Test
    void registerReader_shouldChangeCounter() {
        library.registerReader("Adelina");
        library.registerReader("Nick");
        Reader reader1 = library.getReaderByID(1L);
        Reader reader2 = library.getReaderByID(2L);

        assertThat(reader2.getId()).isEqualTo(2L);
    }

    @Test
    void borrowBook_shouldChangeStatus() {
        library.addBook("1984", "George Orwell");
        Book book = library.getBookByID(1L);
        library.registerReader("Adelina");
        Reader reader = library.getReaderByID(1L);
        library.borrowBook(book.getId(), reader.getId());

        assertThat(book.getStatus()).isEqualTo(BookStatus.BORROWED);
    }

    @Test
    void borrowBook_shouldThrowExceptionBorrowed() {
        library.addBook("1984", "George Orwell");
        Book book = library.getBookByID(1L);
        library.registerReader("Adelina");
        library.registerReader("Nick");
        Reader reader1 = library.getReaderByID(1L);
        Reader reader2 = library.getReaderByID(2L);
        library.borrowBook(book.getId(), reader1.getId());

        assertThatThrownBy(() -> library.borrowBook(book.getId(), reader2.getId())).isInstanceOf(LibraryException.class);
    }

    @Test
    void borrowBook_shouldThrowExceptionLimit() {
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

        assertThatThrownBy(() -> library.borrowBook(book4.getId(), reader.getId())).isInstanceOf(LibraryException.class);
    }

    @Test
    void returnBook_shouldChangeStatus() {
        library.addBook("1984", "George Orwell");
        Book book = library.getBookByID(1L);
        library.registerReader("Adelina");
        Reader reader = library.getReaderByID(1L);
        library.borrowBook(book.getId(), reader.getId());
        library.returnBook(book.getId(), reader.getId());

        assertThat(book.getStatus()).isEqualTo(BookStatus.AVAILABLE);
        assertThat(book.getBorrowedByID()).isNull();
    }

    @Test
    void returnBook_shouldThrowExceptionStatus() {
        library.addBook("1984", "George Orwell");
        Book book1 = library.getBookByID(1L);
        library.addBook("On the Origin of Species", "Charles Darwin");
        Book book2 = library.getBookByID(2L);
        library.registerReader("Adelina");
        Reader reader = library.getReaderByID(1L);
        library.borrowBook(book1.getId(), reader.getId());

        assertThatThrownBy(() -> library.returnBook(book2.getId(), reader.getId())).isInstanceOf(LibraryException.class);
    }

    @Test
    void returnBook_shouldThrowExceptionReader() {
        library.addBook("1984", "George Orwell");
        Book book1 = library.getBookByID(1L);
        library.registerReader("Adelina");
        Reader reader1 = library.getReaderByID(1L);
        library.registerReader("Nick");
        Reader reader2 = library.getReaderByID(2L);

        library.borrowBook(book1.getId(), reader1.getId());

        assertThatThrownBy(() -> library.returnBook(book1.getId(), reader2.getId())).isInstanceOf(LibraryException.class);
    }

    @Test
    void findBooksByAuthor_shouldBeRightSize() {
        library.addBook("1984", "George Orwell");
        Book book1 = library.getBookByID(1L);
        library.addBook("Animal Farm", "George Orwell");
        Book book2 = library.getBookByID(2L);

        assertThat(library.findBooksByAuthor("George Orwell").size()).isEqualTo(2);
    }

    @Test
    void findAvailableBooks_shouldBeRightSize() {
        library.addBook("1984", "George Orwell");
        Book book1 = library.getBookByID(1L);
        library.addBook("Animal Farm", "George Orwell");
        Book book2 = library.getBookByID(2L);
        library.addBook("On the Origin of Species", "Charles Darwin");
        Book book3 = library.getBookByID(3L);
        library.registerReader("Adelina");
        Reader reader1 = library.getReaderByID(1L);
        library.borrowBook(book3.getId(), reader1.getId());

        assertThat(library.findAvailableBooks().size()).isEqualTo(2);
    }

    @Test
    void findBooksThatReaderHave_shouldBeRightSize() {
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

        assertThat(library.findBooksThatReaderHave(reader1.getId()).size()).isEqualTo(2);
    }

    @Test
    void findBookOrThrow_shouldThrowException() {
        library.registerReader("Adelina");
        Reader reader1 = library.getReaderByID(1L);

        assertThatThrownBy(() -> library.borrowBook(1L, reader1.getId())).isInstanceOf(LibraryException.class);
    }

    @Test
    void findReaderOrThrow_shouldThrowException() {
        library.addBook("1984", "George Orwell");
        Book book = library.getBookByID(1L);

        assertThatThrownBy(() -> library.borrowBook(book.getId(), 1L)).isInstanceOf(LibraryException.class);
    }
}