package com.goalmeister.model;

import org.mongojack.Id;

public class Goal {
	
	@Id
	public String id;
	public String title;
	public String description;
	
}
