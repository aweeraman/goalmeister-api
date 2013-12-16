package com.goalmeister.data;

public class DaoFactory {

	public static GoalDao getGoalDao() {
		return new GoalDaoImpl();
	}
}
