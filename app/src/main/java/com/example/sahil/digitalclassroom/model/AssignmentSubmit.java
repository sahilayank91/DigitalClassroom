package com.example.sahil.digitalclassroom.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class AssignmentSubmit {
    private String assign_id,_id,user_id;
    private ArrayList<String> path = new ArrayList<>();
    private int marks;
    private Timestamp timestamp;

    public AssignmentSubmit(){
    }
    public AssignmentSubmit(String _id,String assign_id,String user_id,ArrayList path,int marks){
        this._id = _id;
        this.assign_id = assign_id;
        this.user_id = user_id;
        this.path = path;
        this.marks = marks;
    }

    public HashMap getMap(){
        HashMap<String, String> assignSub = new HashMap<>();
        assignSub.put("_id",this._id);
        assignSub.put("assign_id",this.assign_id);
        assignSub.put("user_id",this.user_id);
        //TODO add array list
//        assignSub.put("path",path);
        assignSub.put("marks", String.valueOf(marks));
        return assignSub;
    }
}
