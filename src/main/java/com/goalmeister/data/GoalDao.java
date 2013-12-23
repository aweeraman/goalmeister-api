package com.goalmeister.data;

import java.util.List;

import com.goalmeister.model.Goal;

public interface GoalDao {

  List<Goal> list(String tenant);

  Goal save(Goal goal);

  void delete(String id);

  Goal findById(String id);

}
