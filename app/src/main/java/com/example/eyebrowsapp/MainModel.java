package com.example.eyebrowsapp;

public class MainModel {
    Integer langEyebrows;
    String langName;
    Integer langEyebrows_mask;
    public MainModel(Integer langEyebrows,String langName,Integer langEyebrows_mask){
        this.langEyebrows = langEyebrows ;
        this.langName = langName;
        this.langEyebrows_mask = langEyebrows_mask;
    }
    public Integer getLangEyebrows(){
        return langEyebrows;
    }
    public String getLangName(){
        return langName;
    }
}
