package com.library.service;

import com.library.exception.LibraryException;
import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Reader;

import java.util.*;

public class LibraryService {

    private Map <Long, Book> bookStorage = new HashMap<Long, Book>();
    private Map <Long, Reader> readerStorage = new HashMap<Long, Reader>();
    private Map <Long, List<Long>> readerBooks = new HashMap<Long, List<Long>>();

    private long bookCounterForID = 1;
    private long readerCounterForID = 1;

    public void addBook(String title, String author){
        Book book= new Book(bookCounterForID, title, author, BookStatus.AVAILABLE);
        bookStorage.put (book.getId(), book);
        bookCounterForID ++;
    }

    public void registerReader(String name){
        Reader reader = new Reader(readerCounterForID, name);
        readerStorage.put(readerCounterForID, reader);
        readerBooks.put(readerCounterForID, new ArrayList<>());

        readerCounterForID++;
    }

    public void borrowBook(Long bookID, Long readerID){
        var book = findBookOrThrow(bookID);
        var reader = findReaderOrThrow(readerID);

        if(book.getStatus() == BookStatus.BORROWED){
            throw new LibraryException("Book is already borrowed");
        }
        List<Long> booksThatReaderHave = readerBooks.get(readerID);
        if(booksThatReaderHave.size() >= 3){
            throw new LibraryException("Reader already have max amount of books!");
        }
        book.setStatus(BookStatus.BORROWED);
        book.setBorrowedByID(reader.getID());
        booksThatReaderHave.add(book.getId());
    }

    private Book findBookOrThrow(Long bookID){
        var book = bookStorage.get(bookID);
        if(book == null) {
            throw new LibraryException("Book with ID" + bookID + " not found");
        }
        return book;
    }

    private Reader findReaderOrThrow(Long readerID){
        var reader = readerStorage.get(readerID);
        if(reader == null){
            throw  new LibraryException("Reader with ID" + readerID + " not found");
        }
        return reader;
    }
}

