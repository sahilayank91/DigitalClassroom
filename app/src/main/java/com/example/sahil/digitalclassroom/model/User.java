package com.example.sahil.digitalclassroom.model;


import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class User {

	private String _id;
	private String name,email,password,college_id,phone,otp,department;

	public String getPathImage() {
		return pathImage;
	}

	public void setPathImage(String pathImage) {
		this.pathImage = pathImage;
	}

	private String pathImage;

	private int role;

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

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
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
		if (author.has("role")) this.role = author.getInt("role");
		if (author.has("otp")) this.otp = author.getString("otp");
		if (author.has("pathImage"))this.pathImage = author.getString("pathImage");
		if (author.has("department")) this.department = author.getString("department");
	}

	/*Sending data to the database*/
	public User(String email, String name, String password,String phone,String otp,String department,String _id,String college_id,int role,String pathImage){
		this.email = email;
		this._id = _id;
		this.name = name;
		this.password = password;
		this.phone = phone;
		this.otp = otp;
		this.department = department;
		this.college_id = college_id;
		this.role = role;
		this.pathImage = pathImage;

	}
	@Override
	public boolean equals(Object obj) {
		User user=(User)obj;
		if(user.get_id()==this._id)
			return true;
		else return false;
	}

	public HashMap<String, String > getMap(){
		HashMap<String, String> users = new HashMap<>();
			users.put("_id",this._id);
			users.put("email",this.email);
			users.put("password",this.password);
			users.put("name",this.name);
			users.put("phone",this.phone);
			users.put("otp",this.otp);
			users.put("college_id",this.college_id);
			users.put("role", String.valueOf(this.role));
			users.put("department",this.department);
			users.put("pathImage",this.pathImage);
			return users;
	}


}
