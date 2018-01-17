package com.example.sahil.digitalclassroom.model;


import java.sql.Timestamp;
import java.util.HashMap;

public class Comment {
    private String comment_id,post_id,user_id;
    private Timestamp timestamp;

    public Comment(){
    }
    public Comment(String comment_id,String discuss_id,String user_id,Timestamp timestamp){
        this.comment_id = comment_id;
        this.post_id = discuss_id;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public HashMap getMap(){
        HashMap<String, String> comment = new HashMap<>();
        comment.put("comment_id",this.comment_id);
        comment.put("discuss_id",this.post_id);
        comment.put("user_id",this.user_id);
        comment.put("timestamp",String.valueOf(this.timestamp));
        return comment;
    }

}
