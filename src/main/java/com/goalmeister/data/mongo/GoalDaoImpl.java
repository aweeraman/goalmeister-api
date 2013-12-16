package com.goalmeister.data.mongo;

import com.goalmeister.data.GoalDao;

public class GoalDaoImpl extends AbstractDao implements GoalDao {

	@Override
	public String getData() {
		getDb();
		return "Goal Data";
	}

}
