package com.example.s_car.Local_Database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    ////////////////// OwnerData Entity ////////////////////
    @Insert
    void insertOwner(OwnerData ownerData);
    @Delete
    void deleteOwner(OwnerData ownerData);
    @Query("Select * from Owner_Table")
    List<OwnerData> getOwnerData();

    ////////////////// Other Driver Entity ////////////////////////
    @Insert
    void insertOtherDriver(OtherDriversData otherDriversData);
    @Delete
    void deleteOtherDriver(OtherDriversData otherDriversData);
    @Query("Select * from OtherDrivers_Table")
    List<OtherDriversData> getAllOtherDrivers();
}
