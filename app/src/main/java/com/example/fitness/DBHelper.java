package com.example.fitness;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "users.db";

    public DBHelper(Context context) {
        super(context, "users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, name TEXT, work TEXT, totalSteps INTEGER NOT NULL DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
    }

    public Boolean insertData(String username, String password, String name, String work) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", username);
        values.put("password", password);
        values.put("name", name);
        values.put("work", work);
        values.put("totalSteps", 0);

        long result = db.insert("users", null, values);
        if (result == -1) return false;
        else return true;
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=?", new String[]{username});
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=? and password=?", new String[]{username, password});
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    public String getName(String name){
        // достаем логин
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=? and password=?", new String[]{name});
        System.out.println(cursor + " - name");
        if (cursor.getCount() > 0) return cursor.toString();
        else return "noName";
    }

    public boolean updateTotalStep(int totalStep){
        SQLiteDatabase db = this.getWritableDatabase();
        //String rawQuery = "update <users> set totalSteps="+totalStep;
        ContentValues cv = new ContentValues();
        cv.put("totalSteps", totalStep);
        db.update("users", cv, "totalSteps=?", null);
        return true;
    }
    public boolean updateName(String newName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("totalSteps", newName);
        db.update("users", cv, "name = ?", new String[] { newName });
        return true;
    }

}















