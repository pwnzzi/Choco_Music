package com.example.choco_music.model;

import java.io.Serializable;

public class Music_Playlist_Data implements Serializable {

    private String title;
    private String vocal;
    private String music_url;
    private String album_url;


    public void setTItle(String title){ this.title = title;}

    public void setVocal(String vocal){ this.vocal = vocal;}

    public void setMusic_url(String music_url){ this.music_url = music_url;}

    public void setAlbum_url(String album_url){ this.album_url = album_url;}



    public String getTitle(){
        return this.title;
    }

    public String getVocal(){
        return this.vocal;
    }

    public String getMusic_url(){
        return this.music_url;
    }

    public String getAlbum_url(){
        return this.album_url;
    }





}
