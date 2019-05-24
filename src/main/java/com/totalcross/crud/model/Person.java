package com.totalcross.crud.model;


public class Person {
    private int	id;
    private String name;
    private String born;
    private String number;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBorn() {
        return born;
    }
    public void setBorn(String date) {
        this.born = date;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


}
