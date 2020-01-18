package com.example.choco_music.model;

import java.io.Serializable;

public class ChartData implements Serializable {

    private String title;
    private String vocal;
    private String fileurl;
    private boolean type;
    private  String genre;
    // 자작곡이면 True
    private int album;

    public ChartData(String title, String vocal, String fileurl, boolean type){
        this.title = title;
        this.vocal = vocal;
        this.fileurl = fileurl;
        this.type = type;
    }

    public int getAlbum(){return this.album;}

    public String getGenre(){return this.genre;}

    public String getTitle(){
        return this.title;
    }

    public String getVocal(){
        return this.vocal;
    }

    public String getFileurl(){
        return this.fileurl;
    }

    public boolean getType(){
        return this.type;
    }
}
