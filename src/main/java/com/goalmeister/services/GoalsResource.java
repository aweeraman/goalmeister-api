package com.goalmeister.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Goals service
 */
@Path("goals")
public class GoalsResource extends AbstractResource {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String listAll() {
		return goalDao.listAll();
	}
}
