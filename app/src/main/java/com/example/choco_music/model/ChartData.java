package com.example.choco_music.model;

public class ChartData {

    private int img;
    private String text;

    public ChartData(int img, String text){
        this.img = img;
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public int getImg(){
        return this.img;
    }
}
