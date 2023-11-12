package com.trodev.wifibillmanageruser.models;

public class StatusModel {

    String status, name, mobile, user_token, packages, date, time, uid;

    public StatusModel() {
    }

    public StatusModel( String status, String name, String mobile, String user_token, String packages, String date, String time, String uid) {

        this.status = status;
        this.name = name;
        this.mobile = mobile;
        this.user_token = user_token;
        this.packages = packages;
        this.date = date;
        this.time = time;
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
