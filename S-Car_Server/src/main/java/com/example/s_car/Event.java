package com.example.s_car;

import java.io.Serializable;

public class Event implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    int id =0;
    int ownerId=0;
    String title ="";
    String date = "";
    String time="";
    String address1 ="";
    String town= "";
    String county ="";

    public Event() {
    }

    public Event(int id, int ownerId, String title, String date, String time, String address1, String town, String county) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.date = date;
        this.time = time;
        this.address1 = address1;
        this.town = town;
        this.county = county;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFullAddress(){
        return address1+", "+town+", "+county+", "+"Ireland";
    }
}
