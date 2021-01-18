package com.example.s_car.Local_Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Owner_Table")
public class OwnerData {
    @PrimaryKey(autoGenerate = false)
    public int id;

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

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String name;
    public String accessKey;
    public OwnerData(int id, String name, String accessKey) {
        this.id = id;
        this.name = name;
        this.accessKey = accessKey;
    }



}
