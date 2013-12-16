package com.goalmeister.data.mongo;

import java.util.LinkedList;
import java.util.List;

import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;

import com.goalmeister.data.GoalDao;
import com.goalmeister.model.Goal;

public class GoalDaoImpl extends AbstractDao implements GoalDao {

	@Override
	public List<Goal> listAll() {
		
		JacksonDBCollection<Goal, String> coll = JacksonDBCollection.wrap(
				db.getCollection("goals"), Goal.class, String.class);
		
		DBCursor<Goal> cursor = coll.find();
		
		List<Goal> goals = new LinkedList<Goal>();
		Goal goal;
		while (cursor.hasNext()) {
			goal = cursor.next();
			goals.add(goal);
		}
		return goals;

//		StringBuffer buf = new StringBuffer();
//		DBCursor cursor = db.getCollection("goals").find();
//		try {
//			while (cursor.hasNext()) {
//				buf.append(cursor.next());
//			}
//		} finally {
//			cursor.close();
//		}
//		return buf.toString();
	}

}
