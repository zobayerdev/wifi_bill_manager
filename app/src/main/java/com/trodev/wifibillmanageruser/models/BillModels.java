package com.trodev.wifibillmanageruser.models;

public class BillModels {

    String aid, name, user_id, packages, mobile, month, price,  date, time, year, bill_no,  uid;

    public BillModels() {
    }

    public BillModels(String aid, String name, String user_id, String packages, String mobile, String month, String price, String date, String time, String year, String bill_no, String uid) {
        this.aid = aid;
        this.name = name;
        this.user_id = user_id;
        this.packages = packages;
        this.mobile = mobile;
        this.month = month;
        this.price = price;
        this.date = date;
        this.time = time;
        this.year = year;
        this.bill_no = bill_no;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
