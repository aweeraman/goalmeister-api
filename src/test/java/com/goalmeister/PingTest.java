package com.goalmeister;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.internal.util.Base64;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.goalmeister.model.Application;
import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;
import com.goalmeister.server.Configuration;
import com.goalmeister.server.Start;

public class PingTest extends AbstractTest {

	private HttpServer server;
	private WebTarget target;
	private User user;
	private UserToken userToken;
	private Application application;

	@Before
	public void setUp() throws Exception {
		// start the server
		server = Start.startServer(null);
		// create the client
		Client c = ClientBuilder.newClient();
		target = c.target(Configuration.getInstance().getBaseUri());

		user = getTestUser();
		application = applicationDao.create();

		getUserToken();
	}

	private void getUserToken() {
		MultivaluedMap<String, String> map = new MultivaluedHashMap<String, String>();
		map.putSingle("grant_type", "password");
		map.putSingle("username", user.email);
		map.putSingle("password", user.password);
		map.putSingle("client_id", application.clientId);
		map.putSingle("client_secret", application.secret);

		userToken = target
				.path("oauth2/token")
				.request()
				.header("Authorization",
						"Basic "
								+ Base64.encodeAsString(application.clientId
										+ ":" + application.secret))
				.buildPost(Entity.form(map)).invoke(UserToken.class);
	}

	@After
	public void tearDown() throws Exception {
		releaseTestUser(user);
		userDao.invalidateToken(userToken.access_token);
		applicationDao.deleteByClientId(application.clientId);
		server.shutdownNow();
	}

	/**
	 * Test to see that the message "Ping!" is sent in the response.
	 */
	@Test
	public void testGetIt() {
		String responseMsg = target.path("ping").request()
				.header("Authorization", authHeader(userToken))
				.get(String.class);

		Assert.assertEquals("Ping!", responseMsg);

		userDao.invalidateToken(userToken.access_token);

		application.enabled = Boolean.FALSE;
		applicationDao.update(application);

		MultivaluedMap<String, String> map = new MultivaluedHashMap<String, String>();
		map.putSingle("grant_type", "password");
		map.putSingle("username", user.email);
		map.putSingle("password", user.password);
		map.putSingle("client_id", application.clientId);
		map.putSingle("client_secret", application.secret);

		Response forbiddenResponse = target
				.path("oauth2/token")
				.request()
				.header("Authorization",
						"Basic "
								+ Base64.encodeAsString(application.clientId
										+ ":" + application.secret))
				.buildPost(Entity.form(map)).invoke();
		
		Assert.assertTrue(forbiddenResponse.getStatus() == 403);
	}
}
