package com.example.choco_music.model;

import java.io.Serializable;

public class HomeData  implements  Serializable{


    private String title;
    private String vocal;
    private String img_path;
    private String genre;

    public HomeData(String title,String vocal, String img_path, String genre){
        this.title = title;
        this.vocal= vocal;
        this.img_path = img_path;
        this.genre = genre;


    }

    public String getGenre(){return this.genre;}

    public String getTitle(){
            return this.title;
        }

    public String getVocal(){ return this.vocal;}

    public String getImg_path(){
            return this.img_path;
        }


}
