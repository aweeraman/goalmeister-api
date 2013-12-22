package com.goalmeister.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;

/**
 * Goals service
 */
@Path("oauth2")
public class OAuth2Resource extends AbstractResource {

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	@Path("/token")
	public Response token(@Context HttpServletRequest request)
			throws OAuthSystemException {

		try {
			OAuthTokenRequest tokenRequest = new OAuthTokenRequest(request);
			OAuthIssuerImpl issuer = new OAuthIssuerImpl(new MD5Generator());

			if (tokenRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.PASSWORD.toString())) {

				if (!isValidUser(tokenRequest.getUsername(),
						tokenRequest.getPassword())) {

					OAuthResponse response = OAuthASResponse
							.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
							.setError(OAuthError.TokenResponse.INVALID_REQUEST)
							.setErrorDescription("invalid username or password")
							.buildJSONMessage();

					return Response.status(response.getResponseStatus())
							.entity(response.getBody()).build();
				}
			} else {
				OAuthResponse response = OAuthASResponse
						.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(
								OAuthError.TokenResponse.UNSUPPORTED_GRANT_TYPE)
						.setErrorDescription("unsupported grant type")
						.buildJSONMessage();

				return Response.status(response.getResponseStatus())
						.entity(response.getBody()).build();
			}

			// Check if a token already exists
			UserToken token = dao.getUserDao().findUserTokenByUsername(tokenRequest.getUsername());
			
			// Create new token otherwise
			if (token == null) {
				String accessToken = issuer.accessToken();
				token = dao.getUserDao().newSession(new UserToken(tokenRequest.getUsername(),
						accessToken));
			}

			// Return the token
			OAuthResponse response = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(token.access_token).setExpiresIn("3600")
					.buildJSONMessage();

			return Response.status(response.getResponseStatus())
					.entity(response.getBody()).build();

		} catch (OAuthProblemException e) {
			OAuthResponse res = OAuthASResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
					.buildJSONMessage();

			return Response.status(res.getResponseStatus())
					.entity(res.getBody()).build();
		}
	}

	private boolean isValidUser(String username, String password) {
		User user = dao.getUserDao().findUser(username);
		if (user != null && password != null && password.equals(user.password)) {
			return true;
		}
		return false;
	}

	@GET
	@Path("/invalidate_token")
	public Response invalidate(@Context HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		String tokens[] = authHeader.split(" ");
		String token = tokens[1];
		
		if (token == null || token.length() == 0) {
			return Response.serverError().build();
		}
		
		dao.getUserDao().invalidateToken(token);
		return Response.ok().build();
	}

}
