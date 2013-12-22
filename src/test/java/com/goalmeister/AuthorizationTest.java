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
		Assert.assertEquals(response.getStatus(), 403);
	}
	
	@Test
	public void allowIfAdmin() {
		User testUser = getUserObject();
		
		// Provide the test user with admin privileges
		user.role = "admin";
		userDao.saveUser(user);

		User createdUser = (User) objectForPost("/admin/users", testUser,
				userToken, User.class);
		Assert.assertEquals(testUser.email, createdUser.email);
		Assert.assertNotNull(createdUser._id);

		// Clean up
		userDao.deleteUserById(createdUser._id);
	}
}
