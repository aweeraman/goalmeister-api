package com.goalmeister.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.goalmeister.model.Goal;

/**
 * Goals service
 */
@Path("goals")
public class GoalsResource extends AbstractResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Goal> list() {
		return goalDao.list();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Goal save(Goal goal) {
		return goalDao.save(goal);
	}
	
	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") String id) {
		goalDao.delete(id);
	}
}
