package com.nikmc.kc.model;

/**
 * Created by NIKMC-I on 25.12.2015.
 */
public class Bid {
    private String FIO;
    private String Phone;
    private String City;
    private String Street;
    private String House;
    private String Content;

    public Bid() {
    }

    public Bid(String FIO, String phone, String city, String street, String house, String content) {
        this.FIO = FIO;
        Phone = phone;
        City = city;
        Street = street;
        House = house;
        Content = content;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getHouse() {
        return House;
    }

    public void setHouse(String house) {
        House = house;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
