package com.goalmeister.services;

import com.goalmeister.data.DaoFactory;
import com.goalmeister.data.GoalDao;
import com.goalmeister.data.UserDao;

public abstract class AbstractResource {

	protected static UserDao userDao = DaoFactory.getUserDao();
	
	protected static GoalDao goalDao = DaoFactory.getGoalDao();

}
