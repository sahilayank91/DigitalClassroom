package com.example.sahil.digitalclassroom.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class Image {
	private String filename;
	private long image_id;
	private Long time;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    private String image_url;
	public Image(){

	}
	public Image(JSONObject image) throws JSONException {
        if(image.has("time"))setTime(image.getLong("time"));
        if(image.has("image_id"))setImage_id(image.getLong("image_id"));
        if(image.has("filename"))setFilename(image.getString("filename"));
        if(image.has("image_url"))setFilename(image.getString("image_url"));

    }

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getImage_id() {
		return image_id;
	}

	public void setImage_id(long image_id) {
		this.image_id = image_id;
	}

	public Date getDate() {
		return new Date(time);
	}
	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
	public void setTime(Date time) {
		this.time = time.getTime();
	}

}
