package com.library.model;

public class Book {
    private Long id;
    private String title;
    private  String author;
    private BookStatus status;
    private Long borrowedByReaderID;

    public Book(){
    }

    public Book(Long id, String title, String author, BookStatus status){
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = status;
    }

    public Long getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public  Long getBorrowedByID() {
        return borrowedByReaderID;
    }

    public void setBorrowedByID(Long borrowedByReaderID) {
        this.borrowedByReaderID = borrowedByReaderID;
    }
}
