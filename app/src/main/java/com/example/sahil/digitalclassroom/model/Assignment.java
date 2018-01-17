package com.example.sahil.digitalclassroom.model;


import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;

public class Assignment {

    private String assign_id,group_id,made_by,name;
    private Uri path;
    private long dueDate;
    private double marks;

    public String getAssign_id() {
        return assign_id;
    }

    public void setAssign_id(String assign_id) {
        this.assign_id = assign_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getMade_by() {
        return made_by;
    }

    public void setMade_by(String made_by) {
        this.made_by = made_by;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getPath() {
        return path;
    }

    public void setPath(Uri path) {
        this.path = path;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public Assignment(){
    }
    public Assignment(String group_id,String assign_id,String made_by,String name,Uri path,long dueDate, double marks){
        this.group_id = group_id;
        this.assign_id = assign_id;
        this.made_by = made_by;
        this.name = name;
        this.path = path;
        this.dueDate = dueDate;
        this.marks = marks;
    }
    public Assignment(String assign_id,String name,Uri path,double marks){
        this.assign_id = assign_id;
        this.path = path;
        this.dueDate = dueDate;
        this.marks = marks;
    }
}
