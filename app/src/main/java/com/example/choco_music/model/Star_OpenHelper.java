package com.example.choco_music.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Star_OpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME= "star";
    private static final String DATABASE= "star.db";
    private static final int DATABASE_VERSION = 2;
    private String sql;
    private Context context;

    private SQLiteDatabase db;

    public Star_OpenHelper(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        onCreate(db);
    }

    public void insertData(int position){
        SQLiteDatabase db= getWritableDatabase();
        db.execSQL("INSERT INTO star VALUES(null,'"+position+"')");
        db.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        sql = "CREATE TABLE IF NOT EXISTS star ( _id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "position INTEGER );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    public boolean check_star(int postition){

        boolean Check_data = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME+" ; ",null);

        while(cursor.moveToNext()){
            int position_db= cursor.getInt(cursor.getColumnIndexOrThrow("position"));
            if(postition == position_db)
                Check_data=true;
        }
        cursor.close();
        return Check_data;
    }
}
