package com.goalmeister;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import junit.framework.Assert;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.goalmeister.model.Goal;
import com.goalmeister.server.Configuration;
import com.goalmeister.server.Start;

public class GoalTest {

	private Client client;
	private ClientConfig clientConfig;
	private HttpServer server;
	private WebTarget target;

	@Before
	public void setUp() throws Exception {
		server = Start.startServer(null);
		clientConfig = new ClientConfig().register(new JacksonFeature());
		client = ClientBuilder.newClient(clientConfig);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	@Test
	public void crudTest() {
		Goal goal = new Goal();
		goal.title = "abc";
		goal.description = "pqr";

		target = client.target(Configuration.getInstance().getBaseUri() + "/goals");
		Goal returnedGoal = target.request().accept("application/json").buildPost(Entity.entity(goal, "application/json")).invoke(Goal.class);
		
		Assert.assertEquals(goal.title, returnedGoal.title);
		
		target = client.target(Configuration.getInstance().getBaseUri() + "/goals/" + returnedGoal._id);
		Goal goalById = target.request().accept("application/json").buildGet()
				.invoke(Goal.class);
		
		Assert.assertEquals(returnedGoal.title, goalById.title);
		
		target = client.target(Configuration.getInstance().getBaseUri() + "/goals/" + goalById._id);
		target.request().buildDelete().invoke();
		Goal deletedGoal = target.request().accept("application/json").buildGet()
				.invoke(Goal.class);
		
		Assert.assertTrue(deletedGoal == null);
	}
}
