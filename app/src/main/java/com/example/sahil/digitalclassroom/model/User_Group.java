package com.example.sahil.digitalclassroom.model;

/**
 * Created by sahil on 1/18/2018.
 */

public class User_Group {
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;

    public String getUser_group_id() {
        return user_group_id;
    }

    public void setUser_group_id(String user_group_id) {
        this.user_group_id = user_group_id;
    }

    private String user_group_id;
    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    private String group_id;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private long time;



    public User_Group(String user_id,String group_id,String user_group_id,Long time){
        this.user_id = user_id;
        this.group_id = group_id;
        this.user_group_id = user_group_id;
        this.time = time;
    }

    public User_Group(){

    }

}
