package com.goalmeister;

import java.util.List;

import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.goalmeister.model.Application;
import com.goalmeister.model.Goal;
import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;

public class GoalsCrudTest extends AbstractTest {

	private User user;
	private UserToken userToken;
	private Application application;

	@Before
	public void setUp() throws Exception {
		startServer();
		user = getTestUserBob();
		application = getApplication();
		userToken = getAuthToken(user, application);
	}

	@After
	public void tearDown() throws Exception {
		releaseApplication(application);
		releaseTestUser(user);
		releaseAuthToken(userToken);
		stopServer();
	}

	public Goal getGoalObject() {
		Goal goal = new Goal();
		goal.title = "abc";
		goal.description = "pqr";
		return goal;
	}

	@Test
	public void crud() {
		Goal goal = getGoalObject();

		// Add a goal
		Goal addedGoal = (Goal) objectForPost("/goals", goal, userToken,
				Goal.class);
		Assert.assertNotNull(addedGoal._id);
		Assert.assertEquals(goal.title, addedGoal.title);

		// Query the goal by id
		Goal goalById = (Goal) objectForGet("/goals/" + addedGoal._id,
				userToken, Goal.class);
		Assert.assertEquals(addedGoal.title, goalById.title);

		// Quering a non-existing id - should return a 404
		Response response = (Response) responseForGet("/goals/bogus_id",
				userToken);
		Assert.assertEquals(404, response.getStatus());

		// Check if a GET requests returns a list of goals
		// There should be only one since it was added above
		List<Goal> goals = (List<Goal>) objectForGet("/goals", userToken,
				List.class);
		Assert.assertEquals(1, goals.size());

		// Delete the goal
		getTarget("/goals/" + addedGoal._id).request()
				.header("Authorization", bearerHeader(userToken)).buildDelete()
				.invoke();
		response = responseForGet("/goals/" + addedGoal._id, userToken);
		Assert.assertEquals(response.getStatus(), 404);

		// Delete again - should get a 404
		response = getTarget("/goals/" + addedGoal._id).request()
				.header("Authorization", bearerHeader(userToken)).buildDelete()
				.invoke();
		Assert.assertEquals(response.getStatus(), 404);

		// Delete a bogus goal
		response = getTarget("/goals/bogus_id").request()
				.header("Authorization", bearerHeader(userToken)).buildDelete()
				.invoke();
		Assert.assertEquals(response.getStatus(), 404);

	}

	@Test
	public void security() {
		Goal goal = getGoalObject();

		// Add a goal
		Goal addedGoal = (Goal) objectForPost("/goals", goal, userToken,
				Goal.class);
		Assert.assertNotNull(addedGoal._id);
		Assert.assertEquals(goal.title, addedGoal.title);

		// Query the goal by id
		Goal goalById = (Goal) objectForGet("/goals/" + addedGoal._id,
				userToken, Goal.class);
		Assert.assertEquals(addedGoal.title, goalById.title);

		// Make a malicious change
		String origTenant = goalById.tenant;
		goalById.tenant = "foobar";

		// Try to save it - should get a 401
		Response response = (Response) responseForPost("/goals", goalById,
				userToken);
		Assert.assertEquals(401, response.getStatus());

		// Revert to the original tenant Id, and attempt to save
		goalById.tenant = origTenant;
		goalById.title = "foo";
		Goal updatedGoal = (Goal) objectForPost("/goals", goalById, userToken,
				Goal.class);
		Assert.assertEquals("foo", updatedGoal.title);

		// Login as a different user
		User alice = getTestUserAlice();
		UserToken aliceToken = getAuthToken(alice, application);

		// Attempt to retrieve the goal created by Bob, should get a 401
		response = (Response) responseForGet("/goals/" + goalById._id,
				aliceToken);
		Assert.assertEquals(401, response.getStatus());

		// Alice trying to delete Bob's goal - should get a 401
		response = getTarget("/goals/" + goalById._id).request()
				.header("Authorization", bearerHeader(aliceToken))
				.buildDelete().invoke();
		Assert.assertEquals(401, response.getStatus());

		// Clean up
		goalDao.delete(goalById._id);
		releaseAuthToken(aliceToken);
		releaseTestUser(alice);
	}
}
