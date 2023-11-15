package com.trodev.wifibillmanageruser.models;

public class BillModels {

    String aid, name, user_id, packages, mobile, month,  date, time, year,  uid;

    public BillModels() {
    }

    public BillModels(String aid, String name, String user_id, String packages, String mobile, String month, String date, String time, String year, String uid) {
        this.aid = aid;
        this.name = name;
        this.user_id = user_id;
        this.packages = packages;
        this.mobile = mobile;
        this.month = month;
        this.date = date;
        this.time = time;
        this.year = year;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
