package com.trodev.wifibillmanageruser.models;

public class SupportModels {

    String aid, name, user_id, mobile,  problem, status, date, time, uid;

    public SupportModels() {
    }

    public SupportModels(String aid, String name, String user_id, String mobile, String problem, String status, String date, String time, String uid) {
        this.aid = aid;
        this.name = name;
        this.user_id = user_id;
        this.mobile = mobile;
        this.problem = problem;
        this.status = status;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
