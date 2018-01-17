package com.example.sahil.digitalclassroom.model;


import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class Post {
    private String post_id;


    private String userId;
    private String group_id;
    public ArrayList<String> getFile() {
        return file;
    }
    public void setFile(ArrayList<String> file) {
        this.file = file;
    }
    private ArrayList<String> file;

    public Uri getImage_url() {
        return image_url;
    }

    public void setImage_url(Uri image_url) {
        this.image_url = image_url;
    }

    private Uri image_url;
    private String text;
    private Long time;


    public Post(String post_id,String group_id,String userId, Long time, Uri image_url, String text){
        this.post_id = post_id;
        this.group_id = group_id;
        this.userId = userId;
        this.time = time;
        this.image_url = image_url;
        this.text= text;
    }


    public Post(String post_id,String userId, Long time, Uri image_url, String text){
        this.post_id = post_id;
        this.userId = userId;
        this.time = time;
        this.image_url = image_url;
        this.text= text;
    }

    public Post(String post_id,String userId, Long time, String text){
        this.post_id = post_id;
        this.userId = userId;
        this.time = time;
        this.text= text;
    }

    public Post(String post_id,Long time, String text, Uri image_url){
        this.post_id = post_id;
        this.time = time;
        this.text= text;
        this.image_url = image_url;
    }






    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }



    public Post(JSONObject post) throws JSONException {
        if(post.has("userId")){
            setUserId(post.getString("userId"));
        }

        if(post.has("file")){
            JSONArray file_array=post.getJSONArray("file");
            ArrayList<String> files=new ArrayList<>();
            for(int i=0;i<file_array.length();i++){
                files.add(file_array.getString(i));
            }
           setFile(files);
        }
        if(post.has("post_id"))setPost_id(post.getString("post_id"));
        if(post.has("text"))setText(post.getString("text"));
        if(post.has("time"))setTime(post.getLong("time"));
        if(post.has("group_id"))setGroup_id(post.getString("group_id"));
    }

    public String getPost_id() {
        return post_id;
    }
    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public Long getTime() {
        return time;
    }
    public void setTime(Long time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
