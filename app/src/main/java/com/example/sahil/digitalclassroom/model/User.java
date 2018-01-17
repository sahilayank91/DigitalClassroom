package com.example.sahil.digitalclassroom.model;


import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class User {

	private String _id;
	private String name,email,password,college_id,phone,department;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    private int role;

	public String getProfile_url() {
		return profile_url;
	}

	public void setProfile_url(String profile_url) {
		this.profile_url = profile_url;
	}

	private String profile_url;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCollege_id() {
		return college_id;
	}

	public void setCollege_id(String college_id) {
		this.college_id = college_id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}


	public User(){

	}

	/*Getting data from firebase*/
	public User(JSONObject author) throws JSONException {
		if (author.has("user_id")) this._id = author.getString("_id");
		if (author.has("name")) this.name = author.getString("name");
		if (author.has("phone")) this.phone = author.getString("phone");
		if (author.has("email")) this.email = author.getString("email");
		if (author.has("college_id")) this.college_id = author.getString("college_id");
		if (author.has("department")) this.department = author.getString("department");
		if (author.has("profile_url")) this.department = author.getString("profile_url");

	}

	/*Sending data to the database*/
	public User(String email, String name, String password,String phone,String department,String _id,String college_id,String profile_url, int role){
		this.email = email;
		this._id = _id;
		this.role = role;
		this.name = name;
		this.password = password;
		this.phone = phone;
		this.department = department;
		this.college_id = college_id;
		this.profile_url = profile_url;

	}
    public User(String email, String name, String password,String phone,String department,String _id,String college_id){
        this.email = email;
        this._id = _id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.department = department;
        this.college_id = college_id;


    }
    public User(String email, String name, String password,String phone,String department,String _id,String college_id, int role){
        this.email = email;
        this._id = _id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.department = department;
        this.college_id = college_id;
        this.role = role;


    }
	@Override
	public boolean equals(Object obj) {
		User user=(User)obj;
		if(user.get_id()==this._id)
			return true;
		else return false;
	}

}
