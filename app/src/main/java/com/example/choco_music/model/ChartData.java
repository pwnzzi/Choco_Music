package com.example.choco_music.model;

public class ChartData {

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
