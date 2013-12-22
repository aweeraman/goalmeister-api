package com.goalmeister.data;

import com.goalmeister.model.Application;

public interface ApplicationDao {

	Application findByClientId(String clientId);

	void deleteByClientId(String clientId);

	Application create();

	Application update(Application application);

}
