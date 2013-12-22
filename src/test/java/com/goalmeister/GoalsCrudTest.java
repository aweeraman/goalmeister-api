package com.goalmeister;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.goalmeister.model.Goal;
import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;
import com.goalmeister.server.Configuration;
import com.goalmeister.server.Start;

public class GoalsCrudTest extends AbstractTest {

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
		userToken = getAuthToken();
	}

	@After
	public void tearDown() throws Exception {
		releaseTestUser(user);
		releaseAuthToken(userToken);
		server.shutdownNow();
	}

	@Test
	public void crudTest() {
		Goal goal = new Goal();
		goal.title = "abc";
		goal.description = "pqr";

		target = client.target(Configuration.getInstance().getBaseUri()
				+ "/goals");
		
		Goal returnedGoal = target.request()
				.header("Authorization", bearerHeader(userToken))
				.accept("application/json")
				.buildPost(Entity.entity(goal, "application/json"))
				.invoke(Goal.class);

		Assert.assertEquals(goal.title, returnedGoal.title);

		target = client.target(Configuration.getInstance().getBaseUri()
				+ "/goals/" + returnedGoal._id);
		
		Goal goalById = target.request()
				.header("Authorization", bearerHeader(userToken))
				.accept("application/json").buildGet().invoke(Goal.class);

		Assert.assertEquals(returnedGoal.title, goalById.title);

		target = client.target(Configuration.getInstance().getBaseUri()
				+ "/goals/" + goalById._id);
		
		target.request().header("Authorization", bearerHeader(userToken))
				.buildDelete().invoke();
		
		Response deletedResponse = target.request()
				.header("Authorization", bearerHeader(userToken))
				.accept("application/json").buildGet().invoke();

		Assert.assertEquals(deletedResponse.getStatus(), 404);
	}
}
