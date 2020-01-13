package com.example.choco_music.model;

import java.io.Serializable;

public class ChartData implements Serializable {

    private String title;
    private String vocal;

    public ChartData(String title, String vocal){
        this.title = title;
        this.vocal = vocal;
    }

    public String getTitle(){
        return this.title;
    }

    public String getVocal(){
        return this.vocal;
    }
}
