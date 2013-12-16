package com.goalmeister.data;

import com.goalmeister.data.mongo.GoalDaoImpl;

public class DaoFactory {

	public static GoalDao getGoalDao() {
		return new GoalDaoImpl();
	}
}
