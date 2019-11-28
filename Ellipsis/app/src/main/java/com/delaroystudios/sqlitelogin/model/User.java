package com.delaroystudios.sqlitelogin.model;


public class User {

    private int id;
    private String name;
    private String email;
    private String post;
    private String workingUnder;
    private String password;

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

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPost(){
        return post;
    }

    public void setPost(String post){
        this.post = post;
    }

    public String getWorkingUnder(){
        return workingUnder;
    }

    public void setWorkingUnder(String workingUnder){
        this.workingUnder = workingUnder;
    }


    public String getPassword(){
       return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
