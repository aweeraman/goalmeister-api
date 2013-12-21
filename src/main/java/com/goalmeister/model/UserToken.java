package com.goalmeister.model;

import org.mongojack.ObjectId;

public class UserToken {
	
	@ObjectId
	public String _id;
	public String email;
	public String token;
	
	public UserToken() {
		// empty - needed for serialization
	}
	
	public UserToken(String email, String token) {
		this.email = email;
		this.token = token;
	}

}
