package com.example.choco_music.model;

import java.io.Serializable;

public class AlbumData implements Serializable {

    private String albumName;
    private String img_path;
    private int id;

    public AlbumData(int id,String albumName, String img_path){
        this.albumName = albumName;
        this.img_path = img_path;
        this.id = id;


    }

    public String getAlbum_name(){
        return this.albumName;
    }

    public int getId(){ return this.id;}

    public String getImg_path(){
        return this.img_path;
    }

}


