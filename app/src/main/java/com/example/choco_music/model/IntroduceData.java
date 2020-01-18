package com.example.choco_music.model;

import java.io.Serializable;

public class IntroduceData implements Serializable {

    private String title;
    private String vocal;
    private String lyrics;
    private String img_path;

    public IntroduceData(String title, String vocal, String lyrics, String img_path){
        this.title = title;
        this.vocal = vocal;
        this.lyrics = lyrics;
        this.img_path = img_path;

    }
    public String getTitle(){
        return this.title;
    }
    public String getImg_path(){
        return this.img_path;}

    public String getLyrics(){
        return this.lyrics;
    }
    public String getVocal(){
        return this.vocal;
    }
}
