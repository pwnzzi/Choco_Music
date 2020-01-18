package com.example.choco_music.model;

import java.io.Serializable;

public class TodaySongData implements Serializable {

    private int songOwn;
    private int songCovered;
    private int id;

    public TodaySongData(int songOwn,int songCovered, int id){
        this.songOwn = songOwn;
        this.songCovered = songCovered;
        this.id = id;


    }

    public int get_songOwn(){
        return this.songOwn;
    }

    public int getId(){ return this.id;}

    public int get_songCovered(){
        return this.songCovered;
    }

}

