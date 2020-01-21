package com.example.choco_music.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class Playlist_Database_OpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "music";
    private static final String DATABASE= "music.db";
    private static final int DATABASE_VERSION = 2;
    private String sql;
    private Context context;


    private SQLiteDatabase db;

    public Playlist_Database_OpenHelper(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        onCreate(db);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        sql = "CREATE TABLE IF NOT EXISTS music ( _id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "title TEXT, vocal TEXT, music_url TEXT, album_url TEXT, type TEXT );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String title, String vocal, String music_url, String album_url, String type){
        SQLiteDatabase db= getWritableDatabase();
        db.execSQL("INSERT INTO music VALUES(null,'"+title+"','"+vocal+"','"+music_url+"','"+album_url+"', '"+type+"')");
        db.close();
    }


    public void deleteData(String title){
        SQLiteDatabase db= getWritableDatabase();
        db.execSQL("DELETE FROM music WHERE title = '" + title + "'");
        db.close();
    }
    public ArrayList<ChartData> get_Music_chart(){

        ArrayList<ChartData> music_playist = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME+" ; ",null);
        StringBuilder stringBuffer= new StringBuilder();
        ChartData music_playlist_data = null;

        while(cursor.moveToNext()){
            music_playlist_data= new ChartData();

            String title= cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String vocal= cursor.getString(cursor.getColumnIndexOrThrow("vocal"));
            String album_url = cursor.getString(cursor.getColumnIndexOrThrow("album_url"));
            String file_url = cursor.getString(cursor.getColumnIndexOrThrow("music_url"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            if(type.equals("자작곡"))
                music_playlist_data.setType(true);
            else
                music_playlist_data.setType(false);
            music_playlist_data.setFileurl(file_url);
            music_playlist_data.setTitle(title);
            music_playlist_data.setVocal(vocal);
            music_playlist_data.setImg_path(album_url);
            stringBuffer.append(music_playlist_data);
            music_playist.add(music_playlist_data);

        }
        cursor.close();
        return music_playist;
    }

}
