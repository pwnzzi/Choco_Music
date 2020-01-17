package com.example.choco_music.model;

import java.io.Serializable;

public class CoverData implements Serializable {

    private String title;
    private String vocal;
    private String music_url;
    private String Album_img_url;
    private  int id;
    private  String genre;
    private  String lyrics;
    private  String comment;
    private  String bucketName;
    private  String lyricist;
    private String fileurl;

    public CoverData(String title, String vocal,String music_url){
        this.title = title;
        this.vocal = vocal;
        this.music_url= music_url;
    }

    public String getTitle(){ return this.title; }

    public String getVocal(){ return this.vocal; }

    public String getFilerul(){return this.fileurl;}

    public String getLyricist() {
        return lyricist;
    }

    public String getGenre() {
        return genre;
    }

    public String getLyrics() {
        return lyrics;
    }

    public String getComment() {
        return comment;
    }

    public String getBucketName() {
        return bucketName;
    }

    public int getId() {
        return id;
    }

}
