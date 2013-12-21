package com.goalmeister;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;
import com.goalmeister.server.Configuration;
import com.goalmeister.server.Start;

public class AuthorizationTest extends AbstractTest {

	private Client client;
	private ClientConfig clientConfig;
	private HttpServer server;
	private WebTarget target;
	private User user;
	private UserToken userToken;

	@Before
	public void setUp() throws Exception {
		server = Start.startServer(null);
		clientConfig = new ClientConfig().register(new JacksonFeature());
		client = ClientBuilder.newClient(clientConfig);
		user = getTestUser();
		userToken = getTemporaryAuthToken();
	}

	@After
	public void tearDown() throws Exception {
		removeTestUser(user);
		removeTemporaryAuthToken(userToken);
		server.shutdownNow();
	}

	@Test
	public void crudTest() {
		User newUser = new User();
		newUser.email = "temp@goalmeister.com";
		newUser.password = "temporary";
		newUser.role = "user";

		target = client.target(Configuration.getInstance().getBaseUri()
				+ "/admin/users");
		
		Response response = target.request()
				.header("Authorization", authHeader(userToken))
				.accept("application/json")
				.buildPost(Entity.entity(newUser, "application/json"))
				.invoke();
		
		Assert.assertTrue(response.getStatus() == 403);
		
		// Provide the test user with admin privileges
		user.role = "admin";
		userDao.saveUser(user);

		User createdUser = target.request()
				.header("Authorization", authHeader(userToken))
				.accept("application/json")
				.buildPost(Entity.entity(newUser, "application/json"))
				.invoke(User.class);
		Assert.assertEquals(newUser.email, createdUser.email);
		Assert.assertNotNull(createdUser._id);
		
		// Clean up
		userDao.deleteUserById(createdUser._id);
	}
}
