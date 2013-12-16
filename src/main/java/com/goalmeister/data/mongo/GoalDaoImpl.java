package com.goalmeister.data.mongo;

import java.util.LinkedList;
import java.util.List;

import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;

import com.goalmeister.data.GoalDao;
import com.goalmeister.model.Goal;

public class GoalDaoImpl extends AbstractDao implements GoalDao {

	JacksonDBCollection<Goal, String> coll = JacksonDBCollection.wrap(
			db.getCollection("goals"), Goal.class, String.class);
	
	@Override
	public List<Goal> list() {
		DBCursor<Goal> cursor = coll.find();	
		List<Goal> goals = new LinkedList<Goal>();
		Goal goal;
		while (cursor.hasNext()) {
			goal = cursor.next();
			goals.add(goal);
		}
		return goals;
	}
	
	public Goal save(Goal goal) {
		return coll.save(goal).getSavedObject();
	}

	@Override
	public void delete(String id) {
		coll.removeById(id);
	}

	@Override
	public Goal findById(String id) {
		return coll.findOneById(id);
	}
}
