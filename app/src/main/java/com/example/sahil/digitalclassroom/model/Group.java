package com.example.sahil.digitalclassroom.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Group {

    private String group_id;
    private String teacher_id;
    private String name;
    private ArrayList<String> students = new ArrayList<>();

    public Group(){

    }
    public Group(String group_id, String teacher_id, String name, ArrayList<String> students){
        this.group_id = group_id;
        this.teacher_id = teacher_id;
        this.name = name;
        this.students = students;
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


    public HashMap getMap(){
        HashMap<String, String> groups = new HashMap<>();
        groups.put("group_id",this.group_id);
        groups.put("teacher_id",this.teacher_id);
        groups.put("name",this.name);
        //TODO add array list students
        return groups;
    }
}

