package com.example.choco_music.model;

import java.io.Serializable;

public class CoverData implements Serializable {
    private int id;
    private String title;
    private String vocal;
    private  String genre;
    private  String lyrics;
    private  String comment;
    private  String bucketName;
    private  String lyricist;
    private String fileurl;
    private int album;

    public CoverData(String title, String vocal, String fileurl){
        this.title = title;
        this.vocal = vocal;
        this.fileurl = fileurl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getVocal() {
        return vocal;
    }

    public String getFileurl() {
        return fileurl;
    }

    public String getGenre(){return genre;}

    public int getAlbum(){return album;}




}
