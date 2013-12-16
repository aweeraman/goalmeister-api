package com.goalmeister.data.mongo;

import com.goalmeister.data.GoalDao;
import com.mongodb.DBCursor;

public class GoalDaoImpl extends AbstractDao implements GoalDao {

	@Override
	public String listAll() {
		StringBuffer buf = new StringBuffer();
		DBCursor cursor = db.getCollection("goals").find();
		try {
			while (cursor.hasNext()) {
				buf.append(cursor.next());
			}
		} finally {
			cursor.close();
		}
		return buf.toString();
	}

}
