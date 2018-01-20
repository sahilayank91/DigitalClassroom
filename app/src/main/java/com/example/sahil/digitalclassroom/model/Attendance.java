package com.example.sahil.digitalclassroom.model;


import java.util.Date;
import java.util.HashMap;

public class Attendance {
    private String taken_by,group_id,user_id;
    private String date;
    private boolean is_present;
    private String groupid_date;

    public String getGroupid_date() {
        return groupid_date;
    }

    public void setGroupid_date(String groupid_date) {
        this.groupid_date = groupid_date;
    }



    public String getTaken_by() {
        return taken_by;
    }

    public void setTaken_by(String taken_by) {
        this.taken_by = taken_by;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIs_present() {
        return is_present;
    }

    public void setIs_present(boolean is_present) {
        this.is_present = is_present;
    }

    public Attendance(){
    }
    public Attendance(String taken_by, String group_id, String user_id, String date, boolean is_present){
        this.taken_by = taken_by;
        this.group_id = group_id;
        this.user_id = user_id;
        this.date = date;
        this.is_present = is_present;
    }

}
