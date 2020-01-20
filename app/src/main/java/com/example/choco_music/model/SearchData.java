package com.example.choco_music.model;

import java.io.Serializable;

public class SearchData implements Serializable {


    private String title;
    private String vocal;
    private String fileurl;
    private String img_path;
    private int album;
    private int id;
    private String getImg_path;

    public SearchData(String title, String vocal,String fileurl,String img_path){
        this.title = title;
        this.vocal = vocal;
        this.fileurl = fileurl;
        this.album = album;
        this.id = id;
        this.img_path = img_path;
    }

    public String getTitle(){
        return this.title;
    }

    public int getAlbum(){return this.album;}

    public int getId(){return this.id;}

    public String getImg_path(){ return this.img_path;}

    public String getVocal(){
        return this.vocal;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setVocal(String vocal){
        this.vocal = vocal;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
}
