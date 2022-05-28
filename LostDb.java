package com.example.myapplication;

public class LostDb {
    private String userName;
    private String desc;
    private String date;
    private String location;
    private int phone;


    public String getUserName() {             //获取用户名
        return userName;
    }


    public String getDate() {
        return date;
    }



    public String getLocation() {                //获取用户密码
        return location;
    }


    public String getDesc() {                //获取用户密码
        return desc;
    }



    public int getPhone() {                   //获取用户ID号
        return phone;
    }


    public LostDb(int phone, String userName, String desc, String date, String location) {  //这里采用用户名和密码,性别，爱好
        super();
        this.phone = phone;
        this.userName = userName;
        this.desc = desc;
        this.date = date;
        this.location = location;
    }
 
}