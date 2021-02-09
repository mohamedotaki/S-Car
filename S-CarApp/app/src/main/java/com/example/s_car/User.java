package com.example.s_car;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    int id = 0;
    int loginID=0;
    String name ="";
    String emailAddress = "";
    String phoneNumber = "";
    String carNumber = "";
    String password = "";
    String carKey ="";
    int imageId;


    public User() {
    }

    public User(String name, String emailAddress, String phoneNumber, String carNumber, String password, String carKey, int imageId) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.carNumber = carNumber;
        this.password = password;
        this.carKey = carKey;
        this.imageId = imageId;
    }

    public User(int id, int loginID, String name, String emailAddress, String phoneNumber, String carNumber, String password, String carKey, int imageId) {
        this.id = id;
        this.loginID = loginID;
        this.name = name;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.carNumber = carNumber;
        this.password = password;
        this.carKey = carKey;
        this.imageId = imageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoginID() {
        return loginID;
    }

    public void setLoginID(int loginID) {
        this.loginID = loginID;
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

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDrivingPermission(){
        return "";
    }

}
