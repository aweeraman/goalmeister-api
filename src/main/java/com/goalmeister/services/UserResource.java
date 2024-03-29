package com.goalmeister.services;

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

/**
 * Goals service
 */
@Path("admin")
public class UserResource extends AbstractResource {

  @Context
  private SecurityContext securityContext;

  @POST
  @Consumes("application/json")
  @Produces("application/json")
  @Path("/users")
  public Response createUser(User user) {
    if (securityContext.isUserInRole("admin")) {
      return Response.ok().entity(dao.getUserDao().newUser(user)).build();
    }
    return Response.status(Status.UNAUTHORIZED).build();
  }

  @DELETE
  @Path("/users/{id}")
  public Response deleteUser(@PathParam("id") String id) {
    if (securityContext.isUserInRole("admin")) {
      dao.getUserDao().deleteUserById(id);
      return Response.ok().build();
    }
    return Response.status(Status.UNAUTHORIZED).build();
  }
}
