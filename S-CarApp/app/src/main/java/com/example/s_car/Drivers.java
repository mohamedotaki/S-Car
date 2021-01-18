package com.example.s_car;

import java.util.Date;

public class Drivers {
    int id=0;
    String name ="";
    String phoneNumber="";
    boolean admin = false;
    String validTill = null ;

    public Drivers(int id, String name, String phoneNumber, boolean admin, String validTill) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.admin = admin;
        this.validTill = validTill;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getValidTill() {
        return validTill;
    }

    public void setValidTill(String validTill) {
        this.validTill = validTill;
    }
}
