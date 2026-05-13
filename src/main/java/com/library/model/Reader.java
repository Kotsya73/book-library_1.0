package com.library.model;

public class Reader {
    private Long id;
    private String name;

    public Reader(){
    }

    public Reader(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public Long getID(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
