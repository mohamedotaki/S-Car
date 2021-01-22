package com.example.s_car;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    int id = 0;
    String name ="";
    String emailAddress = "";
    String phoneNumber = "";
    String carNumber = "";
    String password = "";
    String carKey ="";
    boolean isOwner = false;
    Date drivingPermission = null;


    public User() {
    }

    public User(String name, String emailAddress, String phoneNumber, String carNumber, String password) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.carNumber = carNumber;
        this.password = password;
    }

    public User(int id, String name, String emailAddress, String phoneNumber, String carNumber, String password, String carKey, boolean isOwner, Date drivingPermission) {
        this.id = id;
        this.name = name;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.carNumber = carNumber;
        this.password = password;
        this.carKey = carKey;
        this.isOwner = isOwner;
        this.drivingPermission = drivingPermission;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCarKey() {
        return carKey;
    }

    public void setCarKey(String carKey) {
        this.carKey = carKey;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public Date getDrivingPermission() {
        return drivingPermission;
    }

    public void setDrivingPermission(Date drivingPermission) {
        this.drivingPermission = drivingPermission;
    }
}
