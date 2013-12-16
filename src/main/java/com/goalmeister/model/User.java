package com.goalmeister.model;

import org.mongojack.ObjectId;

public class User {
	
	@ObjectId
	public String _id;
	public String email;
	public String password;
	public String role;

}
