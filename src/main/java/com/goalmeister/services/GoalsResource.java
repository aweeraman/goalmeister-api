package com.goalmeister.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.goalmeister.data.DaoFactory;
import com.goalmeister.data.GoalDao;

/**
 * Ping service
 */
@Path("goals")
public class GoalsResource {

	GoalDao goalDao = DaoFactory.getGoalDao();

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return goalDao.getData();
	}
}
