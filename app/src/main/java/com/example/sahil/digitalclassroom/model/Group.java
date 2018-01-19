package com.example.sahil.digitalclassroom.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Group {

    private String group_id;
    private String teacher_id;
    private String name;
    private ArrayList<String> students = new ArrayList<>();

    public String getGroup_join_code() {
        return group_join_code;
    }

    public void setGroup_join_code(String group_join_code) {
        this.group_join_code = group_join_code;
    }

    private String group_join_code;

    public Group(){

    }
    public Group(String group_id, String teacher_id, String name, ArrayList<String> students){
        this.group_id = group_id;
        this.teacher_id = teacher_id;
        this.name = name;
        this.students = students;
    }
    /*For the group just created*/
    public Group(String group_id, String teacher_id, String name){
        this.group_id = group_id;
        this.teacher_id = teacher_id;
        this.name = name;
    }

    public Group(String group_id, String teacher_id, String name,String group_join_code){
        this.group_id = group_id;
        this.teacher_id = teacher_id;
        this.name = name;
        this.group_join_code = group_join_code;
    }
    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

