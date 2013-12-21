package com.goalmeister.data.mongo;

import java.util.LinkedList;
import java.util.List;

import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;

import com.goalmeister.data.GoalDao;
import com.goalmeister.model.Goal;

public class GoalDaoImpl extends AbstractDao implements GoalDao {

	private JacksonDBCollection<Goal, String> col_goals = JacksonDBCollection.wrap(
			db.getCollection("goals"), Goal.class, String.class);
	
	@Override
	public List<Goal> list() {
		DBCursor<Goal> cursor = col_goals.find();	
		List<Goal> goals = new LinkedList<Goal>();
		Goal goal;
		while (cursor.hasNext()) {
			goal = cursor.next();
			goals.add(goal);
		}
		return goals;
	}
	
	public Goal save(Goal goal) {
		return col_goals.save(goal).getSavedObject();
	}

	@Override
	public void delete(String id) {
		col_goals.removeById(id);
	}

	@Override
	public Goal findById(String id) {
		return col_goals.findOneById(id);
	}
}
