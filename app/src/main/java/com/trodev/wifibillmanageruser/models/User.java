package com.trodev.wifibillmanageruser.models;

public class User {

    public  String uname, num, email, pass, user_token, nid, packages, prices, uid;

    public User() {
    }

    public User(String uname, String num, String email, String pass, String user_token, String nid, String packages, String prices) {
        this.uname = uname;
        this.num = num;
        this.email = email;
        this.pass = pass;
        this.user_token = user_token;
        this.nid = nid;
        this.packages = packages;
        this.prices = prices;
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
