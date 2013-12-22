package com.goalmeister.data;

import com.goalmeister.mongo.ApplicationDaoImpl;
import com.goalmeister.mongo.GoalDaoImpl;
import com.goalmeister.mongo.UserDaoImpl;

public class DaoFactory {

	private DaoFactory() {
		// private constructor
	}

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
