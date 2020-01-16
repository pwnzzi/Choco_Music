package com.example.choco_music.model;

import java.io.Serializable;

public class SearchData implements Serializable {


    private String title;
    private String vocal;

    public SearchData(String title, String vocal){
        this.title = title;
        this.vocal = vocal;
    }

    public String getTitle(){
        return this.title;
    }

    public String getVocal(){
        return this.vocal;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setVocal(String vocal){
        this.vocal = vocal;
    }
}
