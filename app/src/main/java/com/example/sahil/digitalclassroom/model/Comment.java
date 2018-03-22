package com.example.sahil.digitalclassroom.model;

import java.util.Date;

public class Comment {
private String comment_id;
private String author;
private String text;
private Long time;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    private String post_id;

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    private String profile_url;


public Comment(){

}

public Comment(String comment_id,String author, String text, Long time){
    this.comment_id = comment_id;
    this.author = author;
    this.text = text;
    this.time = time;

}

    public Comment(String comment_id,String author, String text, Long time,String profile_url){
        this.comment_id = comment_id;
        this.author = author;
        this.profile_url = profile_url;
        this.text = text;
        this.time = time;
    }

    public Comment(String comment_id,String author, String text, Long time,String profile_url,String post_id){
        this.comment_id = comment_id;
        this.author = author;
        this.profile_url = profile_url;
        this.text = text;
        this.time = time;
        this.post_id = post_id;
    }



    public String getComment_id() {
	return comment_id;
}

public void setComment_id(String comment_id) {
	this.comment_id = comment_id;
}
public String getAuthor() {
	return author;
}

public void setAuthor(String author) {
	this.author = author;
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


}
