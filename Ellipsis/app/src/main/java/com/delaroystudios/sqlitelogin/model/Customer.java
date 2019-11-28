package com.delaroystudios.sqlitelogin.model;

public class Customer {

    private int id;
    private String app;
    private String name;
    private String document;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getApp(){
        return app;
    }

    public void setApp(String app){
        this.app = app;
    }

    public String getDocument(){
        return document;
    }

    public void setDocument(String document){
        this.document = document;
    }

}
