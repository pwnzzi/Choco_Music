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

    public void insertData(int position,int star_point,String title, String vocal, String file_url){
        SQLiteDatabase db= getWritableDatabase();
        db.execSQL("INSERT INTO star VALUES(null,'"+position+"','"+star_point+"','"+title+"','"+vocal+"','"+file_url+"')");
        db.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        sql = "CREATE TABLE IF NOT EXISTS star ( _id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "position INTEGER, star_point INTEGER , title TEXT, vocal TEXT, file_url TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    public int check_star(String title, String vocal, String file_url){
        int star_point = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME+" ; ",null);

        while(cursor.moveToNext()){
            String title_db = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String vocal_db = cursor.getString(cursor.getColumnIndexOrThrow("vocal"));
            String file_url_db = cursor.getString(cursor.getColumnIndexOrThrow("file_url"));

            if(title.equals(title_db) && vocal.equals(vocal_db) && file_url.equals(file_url_db)){
                star_point = cursor.getInt(cursor.getColumnIndexOrThrow("star_point"));
            }
        }
        cursor.close();
        return star_point;
    }
}
