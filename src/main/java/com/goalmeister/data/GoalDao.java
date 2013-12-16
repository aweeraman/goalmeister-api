package com.goalmeister.data;

import java.util.List;

import com.goalmeister.model.Goal;

public interface GoalDao {

	public List<Goal> list();
	
	public Goal save(Goal goal);
		
	public Goal delete(String id);
	
}
