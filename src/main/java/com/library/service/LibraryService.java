package com.library.service;

import com.library.model.Book;
import com.library.model.Reader;

import java.util.*;

public class LibraryService {

    private Map <Long, Book> bookStorage = new HashMap<Long, Book>();
    private Map <Long, Reader> readerStorage = new HashMap<Long, Reader>();
    private Map <Long, List<Long>> readerBooks = new HashMap<Long, List<Long>>();

    private long bookCounterForID = 1;
    private long readerCounterForID = 1;

}

