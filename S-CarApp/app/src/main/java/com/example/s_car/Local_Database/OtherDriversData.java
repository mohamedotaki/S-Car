package com.example.s_car.Local_Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "OtherDrivers_Table")
public class OtherDriversData {
    @PrimaryKey(autoGenerate = false)
    public int id;
    public int ownerId;
    public String name;
    public String accessKey;
    public boolean admin;
    public String permissionTime;
}
