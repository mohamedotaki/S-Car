package com.example.s_car.Local_Database;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@androidx.room.Database(entities = {OtherDriversData.class, OwnerData.class}, version = 1)
public abstract class Local_Database extends RoomDatabase {
    private static Local_Database instance;

    public abstract Dao daoAccess();

    public static synchronized Local_Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Local_Database.class, "S_Car_Database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private Dao daoAccess;

        private PopulateDbAsyncTask(Local_Database db) {
            daoAccess = db.daoAccess();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            OwnerData ownerData = new OwnerData(1, "moh", "aaaa");
            daoAccess.insertOwner(ownerData);

            return null;
        }
    }
}
