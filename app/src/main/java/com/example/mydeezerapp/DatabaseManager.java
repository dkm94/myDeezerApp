package com.example.mydeezerapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseManager extends SQLiteOpenHelper {
    // Créer une DB
    //singleton

    private static final String DATABASE_NAME = "mydb.sqlite";
    private static final int CURRENT_DB_VERSION = 1;
    private static DatabaseManager instance;

    public static DatabaseManager getInstance(Context context) {
        if(instance==null){
            instance= new DatabaseManager(context);
        }
        return instance;
    }

    private DatabaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, CURRENT_DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table favorite " + "(id INTEGER, title TEXT, artist TEXT, album TEXT,"+
                "sampleUrl TEXT, link TEXT, coverUrl TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        switch(oldVersion){
            case 1:

        }
    }
}
