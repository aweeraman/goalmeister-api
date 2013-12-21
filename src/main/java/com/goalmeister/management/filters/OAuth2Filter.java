package com.goalmeister.management.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.server.ContainerRequest;

import com.goalmeister.data.ApplicationDao;
import com.goalmeister.data.DaoFactory;
import com.goalmeister.data.UserDao;
import com.goalmeister.management.security.Credentials;
import com.goalmeister.model.Application;
import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;

@Provider
public class OAuth2Filter implements ContainerRequestFilter,
		ContainerResponseFilter {

	private final String TOKEN_ENDPOINT = "/oauth2/token";

	private UserDao userDao = DaoFactory.getUserDao();
	private ApplicationDao applicationDao = DaoFactory.getApplicationDao();

	@Override
	public void filter(ContainerRequestContext request,
			ContainerResponseContext response) throws IOException {
	}

	@Override
	public void filter(ContainerRequestContext request) throws IOException {

		// All endpoints other than the token endpoint should be routed through
		// the bearer token authorization process
		if (!request.getUriInfo().getPath().equals(TOKEN_ENDPOINT)) {
			String authHeader = request
					.getHeaderString(ContainerRequest.AUTHORIZATION);

			if (authHeader == null || (!authHeader.startsWith("Bearer "))) {
				
				// Return 403 if no bearer token is present
				request.abortWith(Response.status(Status.FORBIDDEN).build());
			} else {
				String[] tokens = authHeader.split(" ");
				UserToken token = userDao.findUserToken(tokens[1]);
				if (token != null) {
					// Tokens do not expire. If at a later point it needs to be expired
					// the check would need to be done here.
					User user = userDao.findUser(token.email);
					
					// Set the security context so that further authorization
					// can be performed in the RESTful services
					request.setSecurityContext(new Credentials(user));
				} else {
					
					// Invalid token. Possible break-in attempt.
					// TODO log this
					request.abortWith(Response.status(Status.FORBIDDEN).build());
				}
			}
		} else {
			// Handle basic authentication for the token endpoint
			String authHeader = request
					.getHeaderString(ContainerRequest.AUTHORIZATION);
			
			if (authHeader == null || (!authHeader.startsWith("Basic "))) {
				
				// The token endpoint requires BASIC authentication
				request.abortWith(Response.status(Status.FORBIDDEN).build());
				
			} else {
				String[] tokens = authHeader.split(" ");
				tokens = Base64.decodeAsString(tokens[1]).split(":");
				
				Application app = applicationDao.findByClientId(tokens[0]);
				
				if (! ((app != null) && app.secret.equals(tokens[1])) && app.enabled.booleanValue()) {
					request.abortWith(Response.status(Status.FORBIDDEN).build());
				}
			}
		}
	}
}
