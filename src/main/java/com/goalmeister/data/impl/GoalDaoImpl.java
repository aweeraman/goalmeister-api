package com.goalmeister.data.impl;

import java.util.LinkedList;
import java.util.List;

import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;

import com.goalmeister.data.GoalDao;
import com.goalmeister.model.Goal;
import com.mongodb.BasicDBObject;

public class GoalDaoImpl extends AbstractDao implements GoalDao {

  private JacksonDBCollection<Goal, String> goals = JacksonDBCollection.wrap(
      db.getCollection("goals"), Goal.class, String.class);

  @Override
  public List<Goal> list(String tenant) {
    BasicDBObject obj = new BasicDBObject("tenant", tenant);
    DBCursor<Goal> cursor = goals.find(obj);
    List<Goal> goalList = new LinkedList<Goal>();
    Goal goal;
    while (cursor.hasNext()) {
      goal = cursor.next();
      goalList.add(goal);
    }
    return goalList;
  }

  public Goal save(Goal goal) {
    return goals.save(goal).getSavedObject();
  }

  @Override
  public void delete(String id) {
    goals.removeById(id);
  }

  @Override
  public Goal findById(String id) {
    return goals.findOneById(id);
  }
}
