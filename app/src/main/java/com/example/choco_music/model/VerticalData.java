package com.example.choco_music.model;

import java.io.Serializable;

public class VerticalData implements Serializable {


    private String title;
    private  String vocal;
    private  int id;
    private  String genre;
    private  String lyrics;
    private  String comment;
    private  String bucketName;
    private  String lyricist;
    private String fileurl;

    public VerticalData(int img, String title, String vocal){

        this.title = title;
        this.title = vocal;

    }

    public String getTitle(){
        return this.title;
    }

    public String getVocal(){
        return this.vocal;
    }

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
