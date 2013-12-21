package com.goalmeister.management.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ContainerRequest;

import com.goalmeister.data.DaoFactory;
import com.goalmeister.data.UserDao;
import com.goalmeister.management.security.Credentials;
import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;

@Provider
public class OAuth2Filter implements ContainerRequestFilter,
		ContainerResponseFilter {

	private final String TOKEN_ENDPOINT = "/oauth2/token";

	private UserDao userDao = DaoFactory.getUserDao();

	@Override
	public void filter(ContainerRequestContext request,
			ContainerResponseContext response) throws IOException {
	}

	@Override
	public void filter(ContainerRequestContext request) throws IOException {

		if (!request.getUriInfo().getPath().equals(TOKEN_ENDPOINT)) {
			String authHeader = request
					.getHeaderString(ContainerRequest.AUTHORIZATION);

			if (authHeader == null || (!authHeader.startsWith("Bearer "))) {
				request.abortWith(Response.status(Status.FORBIDDEN).build());
			} else {
				String[] tokens = authHeader.split(" ");
				UserToken token = userDao.findUserToken(tokens[1]);
				if (token != null) {
					// Tokens do not expire. If at a later point it needs to be expired
					// the check would need to be done here.
					User user = userDao.findUser(token.email);
					request.setSecurityContext(new Credentials(user));
				} else {
					request.abortWith(Response.status(Status.FORBIDDEN).build());
				}
			}
		}
	}
}
