package com.goalmeister.data;

import com.goalmeister.data.mongo.ApplicationDaoImpl;
import com.goalmeister.data.mongo.GoalDaoImpl;
import com.goalmeister.data.mongo.UserDaoImpl;

public class DaoFactory {
	
	public static UserDao getUserDao() {
		return new UserDaoImpl();
	}

	public static GoalDao getGoalDao() {
		return new GoalDaoImpl();
	}
	
	public static ApplicationDao getApplicationDao() {
		return new ApplicationDaoImpl();
	}
}
