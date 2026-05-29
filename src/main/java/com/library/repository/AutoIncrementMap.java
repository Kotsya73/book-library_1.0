package com.library.repository;

import com.library.model.Identifiable;

import java.util.*;


public class AutoIncrementMap<T extends Identifiable> {
    private final Map<Long, T> map = new HashMap<>();
    private long counter = 1;

    public void put(T value) {
        value.setId(counter);
        map.put(counter, value);
        counter++;
    }

    public T get(Long id) {
        return map.get(id);
    }

    public Collection<T> getAll() {
        return new ArrayList<>(map.values());
    }
}
