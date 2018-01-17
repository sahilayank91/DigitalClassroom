package com.example.sahil.digitalclassroom.model;


import java.util.Date;
import java.util.HashMap;

public class Attendence {
    private String taken_by,group_id,user_id;
    private Date date;
    private boolean is_present;

    public Attendence(){
    }
    public Attendence(String taken_by,String group_id,String user_id,Date date,boolean is_present){
        this.taken_by = taken_by;
        this.group_id = group_id;
        this.user_id = user_id;
        this.date = date;
        this.is_present = is_present;
    }


}
