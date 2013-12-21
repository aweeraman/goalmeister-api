package com.goalmeister.services;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.goalmeister.model.Goal;

/**
 * Goals service
 */
@Path("goals")
@PermitAll
public class GoalsResource extends AbstractResource {

	@Context
	private SecurityContext securityContext;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Goal> list() {
		// Pass the tenant information to the DAO so that the data can be
		// filtered based on the authenticated user
		return goalDao.list(securityContext.getUserPrincipal().getName());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response findById(@PathParam("id") String id) {
		try {
			// Check if a goal already exists, and if so, use that for checking
			// ownership
			Goal goal = goalDao.findById(id);
			// Check if the tenant of the returned object is the same as the
			// authenticated user. This will prevent access to other user's data
			// by manually crafting requests.
			if (goal != null) {
				if (!securityContext.getUserPrincipal().getName()
						.equals(goal.tenant)) {
					return Response.status(Status.UNAUTHORIZED).build();
				} else {
					// Security checks passed, return 200 and the object
					return Response.ok().entity(goal).build();
				}
			}
			return Response.status(Status.NOT_FOUND).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response save(Goal goal) {
		if (goal._id != null) {
			// Check if a goal already exists, and if so, use that for checking
			// ownership
			Goal existingGoal = goalDao.findById(goal._id);
			if (!securityContext.getUserPrincipal().getName()
					.equals(existingGoal.tenant)) {
				return Response.status(Status.UNAUTHORIZED).build();
			}
		}
		// Specifically set the tenant id so that it cannot be overridden
		// by the client
		goal.tenant = securityContext.getUserPrincipal().getName();

		// Return 200 and the saved object
		return Response.ok().entity(goalDao.save(goal)).build();
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id) {
		try {
			// Check if a goal already exists, and if so, use that for checking
			// ownership
			Goal existingGoal = goalDao.findById(id);
			if (!securityContext.getUserPrincipal().getName()
					.equals(existingGoal.tenant)) {
				return Response.status(Status.UNAUTHORIZED).build();
			}

			// User has been checked out, go ahead and delete
			goalDao.delete(id);

			// Return 200
			return Response.ok().build();
		} catch (IllegalArgumentException e) {

			// Return 404
			return Response.status(Status.NOT_FOUND).build();
		}
	}
}
