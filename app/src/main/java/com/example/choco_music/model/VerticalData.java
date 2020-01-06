package com.example.choco_music.model;

public class VerticalData {

    private int img;
    private String text;
    private String fileName;
    private String isFile;
    private String filePath;
    private String curPath;

    public VerticalData(int img, String text){
        this.img = img;
        this.text = text;
        this.fileName = fileName;
        this.filePath = filePath;
        this.curPath = curPath;
        this.isFile = isFile;


    }

    public String getText(){
        return this.text;
    }

    public int getImg(){
        return this.img;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCurPath() {
        return curPath;
    }

    public void setCurPath(String curPath) {
        this.curPath = curPath;
    }

    public String getIsFile() {
        return isFile;
    }

    public void setIsFile(String isFile) {
        this.isFile = isFile;
    }


}
