package com.example.s_car;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.s_car.Driver;
import com.example.s_car.User;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "S-CarDatabase";
    private static final String TABLE_USER = "User";
    private static final String KEY_ID = "id";
    private static final String KEY_LOGIN_ID = "loginID";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "emailAddress";
    private static final String KEY_NUMBER = "phoneNumber";
    private static final String KEY_CAR_NUMBER = "carNumber";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CAR_KEY = "carKey";
    private static final String KEY_PERMISSION= "Permission";
    private static final String KEY_IMAGE_ID = "imageId";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_LOGIN_ID + " INTEGER,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_NUMBER + " TEXT,"
                + KEY_CAR_NUMBER + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_CAR_KEY + " TEXT,"
                + KEY_PERMISSION + " TEXT,"
                + KEY_IMAGE_ID + " INTEGER" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    // code to add the new user
    void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId()); //
        values.put(KEY_LOGIN_ID, user.getLoginID()); //
        values.put(KEY_NAME, user.getName()); //
        values.put(KEY_EMAIL, user.getEmailAddress()); //
        values.put(KEY_NUMBER, user.getPhoneNumber()); //
        values.put(KEY_CAR_NUMBER, user.getCarNumber()); //
        values.put(KEY_PASSWORD, user.getPassword()); //
        values.put(KEY_CAR_KEY, user.getCarKey()); //
        values.put(KEY_PERMISSION, user.getDrivingPermission()); //
        values.put(KEY_IMAGE_ID, user.getImageId()); //

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    User getUser(String email,String pass) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[] { KEY_ID,KEY_LOGIN_ID,KEY_NAME,KEY_EMAIL,KEY_NUMBER,
                        KEY_CAR_NUMBER,KEY_PASSWORD,KEY_CAR_KEY,KEY_PERMISSION,KEY_IMAGE_ID },
                KEY_EMAIL + "=?" +" AND "+ KEY_PASSWORD+ "=?",
                new String[] { email, pass }, null, null, null, null);
        if (cursor.getCount() >0) {
            cursor.moveToFirst();
            User user;
            if (cursor.getString(8).isEmpty() || cursor.getString(8).equalsIgnoreCase("")) {
                user = new User(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getInt(9));
            } else {
                user = new Driver(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getInt(9)
                        , cursor.getString(8));
            }
            return user;
        }
        // return contact
        return null;
    }

    // code to update the single contact
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId()); //
        values.put(KEY_LOGIN_ID, user.getLoginID()); //
        values.put(KEY_NAME, user.getName()); //
        values.put(KEY_EMAIL, user.getEmailAddress()); //
        values.put(KEY_NUMBER, user.getPhoneNumber()); //
        values.put(KEY_CAR_NUMBER, user.getCarNumber()); //
        values.put(KEY_PASSWORD, user.getPassword()); //
        values.put(KEY_CAR_KEY, user.getCarKey()); //
        values.put(KEY_PERMISSION, user.getDrivingPermission()); //
        values.put(KEY_IMAGE_ID, user.getImageId()); //

        // updating row
        return db.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }

    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return  count;
    }


}
