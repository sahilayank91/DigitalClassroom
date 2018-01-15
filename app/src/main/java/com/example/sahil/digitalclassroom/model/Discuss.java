package com.example.sahil.digitalclassroom.model;


import java.sql.Timestamp;
import java.util.HashMap;

public class Discuss {
    private String group_id,discuss_id,sender_id;
    private Timestamp timestamp;

    public Discuss(){
    }
    public Discuss(String group_id,String discuss_id,String sender_id,Timestamp timestamp){
        this.group_id = group_id;
        this.discuss_id = discuss_id;
        this.sender_id = sender_id;
        this.timestamp = timestamp;
    }

    public HashMap getMap(){
        HashMap<String, String> discuss = new HashMap<>();
        discuss.put("group_id",this.group_id);
        discuss.put("discuss_id",this.discuss_id);
        discuss.put("sender_id",this.sender_id);
        discuss.put("timestamp",String.valueOf(this.timestamp));
        return discuss;
    }
}
