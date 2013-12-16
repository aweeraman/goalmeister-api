package com.goalmeister.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.goalmeister.model.Goal;

/**
 * Goals service
 */
@Path("goals")
public class GoalsResource extends AbstractResource {

	@GET
	@Produces("application/json")
	public List<Goal> listAll() {
		return goalDao.listAll();
	}
}
