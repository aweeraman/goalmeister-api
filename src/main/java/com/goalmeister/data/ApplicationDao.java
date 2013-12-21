package com.goalmeister.data;

import com.goalmeister.model.Application;

public interface ApplicationDao {

	public Application findByClientId(String clientId);
	
	public void deleteByClientId(String clientId);
	
	public Application create();
	
}
