package com.library.repository;

import com.library.model.Reader;

import java.util.*;

public class ReaderRepository {
    private final Map<Long, Reader> readerStorage = new HashMap<>();
    private final Map<Long, List<Long>> readerBooks = new HashMap<>();

    public void saveReader(Reader reader) {
        readerStorage.put(reader.getId(), reader);
    }

    public Reader getReaderById(Long readerId) {
        return readerStorage.get(readerId);
    }

    public void initReaderBooks(Long readerId) {
        readerBooks.put(readerId, new ArrayList<>());
    }

    public List<Long> getReaderBooks(Long readerId) {
        return readerBooks.get(readerId);
    }
}
