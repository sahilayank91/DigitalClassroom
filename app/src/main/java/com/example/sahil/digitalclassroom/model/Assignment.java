package com.example.sahil.digitalclassroom.model;


import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;

public class Assignment {

    private String assign_id,group_id,made_by,name;
    private String path;
    private long dueDate;
    private long uploadedAt;
    private double marks;
    private String fileName;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public long getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(long uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Assignment(){
    }
    public Assignment(String group_id,String assign_id,String made_by,String name,String path,long dueDate, long uploadedAt, double marks, String fileName){
        this.group_id = group_id;
        this.assign_id = assign_id;
        this.made_by = made_by;
        this.name = name;
        this.path = path;
        this.dueDate = dueDate;
        this.marks = marks;
        this.fileName = fileName;
    }

    public Assignment(String assign_id,String name,String path,long dueDate, long uploadedAt,double marks,String fileName){
        this.assign_id = assign_id;
        this.name = name;
        this.path = path;
        this.dueDate = dueDate;
        this.uploadedAt = uploadedAt;
        this.marks = marks;
        this.fileName = fileName;
    }
}
