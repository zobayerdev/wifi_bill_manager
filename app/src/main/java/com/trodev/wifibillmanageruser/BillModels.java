package com.trodev.wifibillmanageruser;

public class BillModels {

    String aid, name, user_id, packages, mobile, date, time, uid;

    public BillModels() {
    }

    public BillModels(String aid, String name, String user_id, String packages, String mobile, String date, String time, String uid) {
        this.aid = aid;
        this.name = name;
        this.user_id = user_id;
        this.packages = packages;
        this.mobile = mobile;
        this.date = date;
        this.time = time;
        this.uid = uid;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
