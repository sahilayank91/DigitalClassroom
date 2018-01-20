package com.example.sahil.digitalclassroom.model;


import java.util.ArrayList;
import java.util.HashMap;

public class Assignment {
    private String assign_id,group_id,made_by,text;
    private ArrayList<String> path = new ArrayList<>();

    public Assignment(){
    }
    public Assignment(String group_id,String assign_id,String made_by,ArrayList path,String text){
        this.group_id = group_id;
        this.assign_id = assign_id;
        this.made_by = made_by;
        this.text = text;
        this.path = path;
    }

    public HashMap getMap(){
        HashMap<String, String> assign = new HashMap<>();
        assign.put("group_id",this.group_id);
        assign.put("assign_id",this.assign_id);
        assign.put("made_by",this.made_by);
        assign.put("text",text);

        return assign;
    }
}
