package com.example.s_car;

import java.io.Serializable;

public class Driver extends User implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    private int ownerId = 0;
    private String drivingPermission = "";

    public  Driver(){
        super();
        ownerId = 0;
        drivingPermission ="";
    }

    public Driver(int id, int loginID, String name, String emailAddress, String phoneNumber, String carNumber, String password, String carKey, int imageId,  String drivingPermission) {
        super(id, loginID, name, emailAddress, phoneNumber, carNumber, password, carKey, imageId);
        this.drivingPermission = drivingPermission;
    }

    public Driver(int id, int loginID, int ownerId, String name, String emailAddress, String phoneNumber, String carNumber, String password, String carKey, int imageId,  String drivingPermission) {
        super(id, loginID, name, emailAddress, phoneNumber, carNumber, password, carKey, imageId);
        this.ownerId = ownerId;
        this.drivingPermission = drivingPermission;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getDrivingPermission() {
        return drivingPermission;
    }

    public void setDrivingPermission(String drivingPermission) {
        this.drivingPermission = drivingPermission;
    }

    @Override
    public String toString() {
        return  super.getDrivingPermission() + drivingPermission;
    }
}