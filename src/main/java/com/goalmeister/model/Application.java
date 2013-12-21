package com.goalmeister.model;

import org.mongojack.ObjectId;

public class Application {
	
	@ObjectId
	public String _id;
	public String clientId;
	public String secret;
	public Boolean enabled;

}
