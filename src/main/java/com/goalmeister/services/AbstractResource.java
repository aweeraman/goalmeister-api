package com.goalmeister.services;

import com.goalmeister.data.DaoFactory;
import com.goalmeister.data.GoalDao;

public abstract class AbstractResource {

	protected GoalDao goalDao = DaoFactory.getGoalDao();

}
