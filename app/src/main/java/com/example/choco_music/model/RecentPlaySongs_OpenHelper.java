package com.example.choco_music.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class RecentPlaySongs_OpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "recent_music";
    private static final String DATABASE= "recent_music.db";
    private static final int DATABASE_VERSION = 2;
    private String sql;
    private Context context;

    public RecentPlaySongs_OpenHelper(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sql = "CREATE TABLE IF NOT EXISTS recent_music ( _id INTEGER PRIMARY KEY AUTOINCREMENT,"+
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
        db.execSQL("INSERT INTO recent_music VALUES(null,'"+title+"','"+vocal+"','"+music_url+"','"+album_url+"', '"+type+"')");
        db.close();
    }
    public void deleteData(int id){
        SQLiteDatabase db= getWritableDatabase();
        db.execSQL("DELETE FROM recent_music WHERE _id = '" + id + "'");
        db.close();
    }

    public ArrayList<ChartData> get_recent_music(){

        ArrayList<ChartData> recent_music_playist = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME+" ; ",null);
        StringBuilder stringBuffer= new StringBuilder();
        ChartData recent_music_playlist_data = null;

        while(cursor.moveToNext()){
            recent_music_playlist_data= new ChartData();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            Log.e("id!!!!!!!!!!!!!!!",""+id);
            String title= cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String vocal= cursor.getString(cursor.getColumnIndexOrThrow("vocal"));
            String album_url = cursor.getString(cursor.getColumnIndexOrThrow("album_url"));
            String file_url = cursor.getString(cursor.getColumnIndexOrThrow("music_url"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            if(type.equals("자작곡"))
                recent_music_playlist_data.setType(true);
            else
                recent_music_playlist_data.setType(false);
            recent_music_playlist_data.setFileurl(file_url);
            recent_music_playlist_data.setTitle(title);
            recent_music_playlist_data.setVocal(vocal);
            recent_music_playlist_data.setImg_path(album_url);
            recent_music_playlist_data.setType_number(3);
            stringBuffer.append(recent_music_playlist_data);
            recent_music_playist.add(recent_music_playlist_data);

        }
        cursor.close();
        return recent_music_playist;
    }
    public Boolean recent_list_Check(String title, String vocal, String file_url, String img_url){

        boolean check_data = true;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME+" ; ",null);

        while(cursor.moveToNext()){
            String title_db= cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String vocal_db= cursor.getString(cursor.getColumnIndexOrThrow("vocal"));
            String album_url_db = cursor.getString(cursor.getColumnIndexOrThrow("album_url"));
            String file_url_db = cursor.getString(cursor.getColumnIndexOrThrow("music_url"));
            if(title.equals(title_db) && vocal.equals(vocal_db) && file_url.equals(file_url_db) && img_url.equals(album_url_db))
                check_data = false;

        }
        cursor.close();
        return check_data;
    }
}
