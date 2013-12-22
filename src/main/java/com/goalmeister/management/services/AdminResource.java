package com.goalmeister.management.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.goalmeister.model.User;
import com.goalmeister.services.AbstractResource;

/**
 * Goals service
 */
@Path("admin")
public class AdminResource extends AbstractResource {

	@Context
	private SecurityContext securityContext;

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/users")
	public Response createUser(User user) {
		if (securityContext.isUserInRole("admin")) {
			return Response.ok().entity(userDao.newUser(user)).build();
		}
		return Response.status(Status.UNAUTHORIZED).build();
	}

	@DELETE
	@Path("/users/{id}")
	public Response deleteUser(@PathParam("id") String id) {
		if (securityContext.isUserInRole("admin")) {
			userDao.deleteUserById(id);
			return Response.ok().build();
		}
		return Response.status(Status.UNAUTHORIZED).build();
	}
}
