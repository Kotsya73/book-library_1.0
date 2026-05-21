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
        if (title == null || title.isBlank()) {
            throw new LibraryException("Title can not be empty!");
        }else if (author == null || author.isBlank()) {
            throw new LibraryException("Author can not be empty!");
        }
        Book book= new Book(bookCounterForID, title, author, BookStatus.AVAILABLE, null);
        bookStorage.put (book.getId(), book);
        bookCounterForID ++;
    }

    public void registerReader(String name){
        if(name == null || name.isBlank()){
            throw new LibraryException("Name can not be empty!");
        }
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
        book.setBorrowedByID(reader.getId());
        booksThatReaderHave.add(book.getId());
    }

    public void returnBook(Long bookID, Long readerID){
        var book = findBookOrThrow(bookID);
        var reader = findReaderOrThrow(readerID);

        if (book.getStatus() == BookStatus.AVAILABLE){
            throw new LibraryException("Book is already available!");
        }
        if(!book.getBorrowedByID().equals(reader.getId())){
            throw new LibraryException("Book is not borrowed by this reader!");
        }
        book.setStatus(BookStatus.AVAILABLE);
        book.setBorrowedByID(null);
        List<Long> booksThatReaderHave = readerBooks.get(readerID);
        booksThatReaderHave.remove(book.getId());
    }

    public List<Book> findBooksByAuthor(String author){
        return bookStorage.values().stream()
                .filter(book -> book.getAuthor().equals(author))
                .toList();
    }

    public List<Book> findAvailableBooks(){
        return bookStorage.values().stream()
                .filter(book -> book.getStatus() == BookStatus.AVAILABLE)
                .toList();
    }

    public List<Book> findBooksThatReaderHave(Long readerID){
        return bookStorage.values().stream()
                .filter(book -> book.getStatus() == BookStatus.BORROWED &&
                        book.getBorrowedByID().equals(readerID))
                .toList();
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

    public Book getBookByID(Long id){
        return  bookStorage.get(id);
    }

    public Reader getReaderByID(Long id){
        return readerStorage.get(id);
    }
}

