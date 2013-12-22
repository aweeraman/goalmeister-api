package com.goalmeister.data.impl;

import com.goalmeister.data.ApplicationDao;
import com.goalmeister.data.GoalDao;
import com.goalmeister.data.UserDao;

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
