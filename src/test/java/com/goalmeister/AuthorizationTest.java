package com.goalmeister;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.goalmeister.model.Application;
import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;

public class AuthorizationTest extends AbstractTest {

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
		releaseTestUser(user);
		releaseAuthToken(userToken);
		stopServer();
	}

	public User getUserObject() {
		User testUser = new User();
		testUser.email = "temp@goalmeister.com";
		testUser.password = "temporary";
		testUser.role = "user";
		return testUser;
	}

	@Test
	public void forbiddenIfNotAdmin() {
		User testUser = getUserObject();

		Response response = responseForPost("/admin/users", testUser, userToken);
		Assert.assertEquals(401, response.getStatus());

		response = (Response) getTarget("/admin/users/" + user._id).request()
				.header("Authorization", bearerHeader(userToken)).buildDelete()
				.invoke();
		Assert.assertEquals(401, response.getStatus());
	}

	@Test
	public void allowIfAdmin() {
		User testUser = getUserObject();

		// Provide the test user with admin privileges
		user.role = "admin";
		userDao.saveUser(user);

		// Check if admin can create users
		User createdUser = (User) objectForPost("/admin/users", testUser,
				userToken, User.class);
		Assert.assertEquals(testUser.email, createdUser.email);
		Assert.assertNotNull(createdUser._id);

		// Check if admin can delete users
		Response response = (Response) getTarget("/admin/users/" + createdUser._id).request()
				.header("Authorization", bearerHeader(userToken)).buildDelete()
				.invoke();
		Assert.assertEquals(200, response.getStatus());
		
		// Provide the test user with admin privileges
		user.role = "user";
		userDao.saveUser(user);
	}
}
