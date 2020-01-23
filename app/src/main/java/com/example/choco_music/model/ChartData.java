package com.example.choco_music.model;

import java.io.Serializable;

public class ChartData implements Serializable {

    private String title;
    private String vocal;
    private String fileurl;
    private boolean type; // 자작곡이면 True
    private String img_path;
    private int type_number;

    public ChartData(){
    }

    public ChartData(String title, String vocal, String fileurl, boolean type, int type_number){
        this.title = title;
        this.vocal = vocal;
        this.fileurl = fileurl;
        this.type = type;
        this.type_number = type_number;
    }
    public void setType(boolean type){this.type = type;}

    public void setType_number(int type_number){this.type_number = type_number;}

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public void setFileurl(String fileurl){ this.fileurl = fileurl;}

    public void setTitle(String title){this.title = title;}

    public void setVocal(String title){this.vocal = title;}

    public int getType_number(){return this.type_number;}

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

    public String getImg_path() {
        return img_path;
    }





}
