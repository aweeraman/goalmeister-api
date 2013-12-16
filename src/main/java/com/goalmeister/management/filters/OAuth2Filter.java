package com.goalmeister.management.filters;

import java.io.IOException;

import javax.security.sasl.AuthenticationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.server.ContainerRequest;

import com.goalmeister.data.DaoFactory;
import com.goalmeister.data.UserDao;
import com.goalmeister.management.security.Credentials;
import com.goalmeister.model.User;

public class OAuth2Filter implements ContainerRequestFilter,
		ContainerResponseFilter {

	private final String TOKEN_ENDPOINT = "/oauth2/token";
	private final String INVALIDATE_TOKEN_ENDPOINT = "/oauth2/invalidate_token";

	private UserDao userDao = DaoFactory.getUserDao();

	@Override
	public void filter(ContainerRequestContext request,
			ContainerResponseContext response) throws IOException {
	}

	@Override
	public void filter(ContainerRequestContext request) throws IOException {
		if (TOKEN_ENDPOINT.equals(request.getUriInfo().getPath())
				|| INVALIDATE_TOKEN_ENDPOINT.equals(request.getUriInfo()
						.getPath())) {

			String authHeader = request
					.getHeaderString(ContainerRequest.AUTHORIZATION);

			if (authHeader == null || (!authHeader.startsWith("Basic "))) {
				throw new AuthenticationException(
						"Basic authentication credentials are required");
			}

			String decodedHeader = Base64.decodeAsString(authHeader
					.subSequence(6, authHeader.length()).toString());
			String[] credentials = decodedHeader.split(":");

			User user = userDao.findUser(credentials[0]);
			if (user != null && credentials[1] != null && credentials[1].equals(user.password)) {
				request.setSecurityContext(new Credentials(user));
			} else {
				throw new AuthenticationException("Access denied");
			}
		}
	}
}
