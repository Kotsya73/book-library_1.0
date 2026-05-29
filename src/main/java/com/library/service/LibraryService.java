package com.library.service;

import com.library.exception.LibraryException;
import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Reader;
import com.library.repository.BookRepository;
import com.library.repository.ReaderRepository;

import java.util.*;

// Split to repository
public class LibraryService {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;


    public LibraryService(BookRepository bookRepository, ReaderRepository readerRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
    }

    public void addBook(String title, String author) {
        if (title == null || title.isBlank()) {
            throw new LibraryException("Title can not be empty!");
        } else if (author == null || author.isBlank()) {
            throw new LibraryException("Author can not be empty!");
        }
        Book book = new Book(null, title, author, BookStatus.AVAILABLE, null);
        bookRepository.saveBook(book);
    }

    public void registerReader(String name) {
        if (name == null || name.isBlank()) {
            throw new LibraryException("Name can not be empty!");
        }
        Reader reader = new Reader(null, name);
        readerRepository.saveReader(reader);
        readerRepository.initReaderBooks(reader.getId());
    }

    public void borrowBook(Long bookID, Long readerID) {
        var book = findBookOrThrow(bookID);
        var reader = findReaderOrThrow(readerID);

        if (book.getStatus() == BookStatus.BORROWED) {
            throw new LibraryException("Book is already borrowed");
        }
        List<Long> booksThatReaderHave = readerRepository.getReaderBooks(readerID);
        if (booksThatReaderHave.size() >= 3) {
            throw new LibraryException("Reader already have max amount of books!");
        }
        book.setStatus(BookStatus.BORROWED);
        book.setBorrowedByID(reader.getId());
        booksThatReaderHave.add(book.getId());
    }

    public void returnBook(Long bookID, Long readerID) {
        var book = findBookOrThrow(bookID);
        var reader = findReaderOrThrow(readerID);

        if (book.getStatus() == BookStatus.AVAILABLE) {
            throw new LibraryException("Book is already available!");
        }
        if (!book.getBorrowedByID().equals(reader.getId())) {
            throw new LibraryException("Book is not borrowed by this reader!");
        }
        book.setStatus(BookStatus.AVAILABLE);
        book.setBorrowedByID(null);
        List<Long> booksThatReaderHave = readerRepository.getReaderBooks(readerID);
        booksThatReaderHave.remove(book.getId());
    }

    public List<Book> findBooksByAuthor(String author) {
        return bookRepository.getAllBooks().stream()
                .filter(book -> book.getAuthor().equals(author))
                .toList();
    }

    public List<Book> findAvailableBooks() {
        return bookRepository.getAllBooks().stream()
                .filter(book -> book.getStatus() == BookStatus.AVAILABLE)
                .toList();
    }

    public List<Book> findBooksThatReaderHave(Long readerID) {
        return bookRepository.getAllBooks().stream()
                .filter(book -> book.getStatus() == BookStatus.BORROWED &&
                        book.getBorrowedByID().equals(readerID))
                .toList();
    }

    private Book findBookOrThrow(Long bookID) {
        var book = bookRepository.getBookById(bookID);
        if (book == null) {
            throw new LibraryException("Book with ID" + bookID + " not found");
        }
        return book;
    }

    private Reader findReaderOrThrow(Long readerID) {
        var reader = readerRepository.getReaderById(readerID);
        if (reader == null) {
            throw new LibraryException("Reader with ID" + readerID + " not found");
        }
        return reader;
    }

    public Book getBookByID(Long id) {
        return bookRepository.getBookById(id);
    }

    public Reader getReaderByID(Long id) {
        return readerRepository.getReaderById(id);
    }
}

