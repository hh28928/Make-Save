package com.example.hammadhanif.cs_477_final_project.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fasterxml.jackson.databind.DeserializationConfig;

public class DatabaseHelper extends SQLiteOpenHelper {

    public final static String NAME = "jobtable";
    public final static String _ID = "_id";
    public final static String ITEM = "item";
    public final static String DESC = "description";
    final static String CREATE_CMD = "CREATE TABLE " +NAME+ "(" +_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ ITEM+" TEXT,"+ DESC +" TEXT)";
    final private static Integer VERSION = 4;
    final Context context;

    public DatabaseHelper(Context context) {
        super(context, "jobtable", null, 3);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
        ContentValues values = new ContentValues();

        //values.put(ITEM, "biceps");
        //db.insert(NAME,null,values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void deleteDatabase ( ) {
        context.deleteDatabase(NAME);
    }

}
