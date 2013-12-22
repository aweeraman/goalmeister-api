package com.goalmeister;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.Base64;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.goalmeister.model.Application;
import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;

public class SecurityTest extends AbstractTest {

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
		Response response = (Response) getTarget(
				"/admin/users/" + createdUser._id).request()
				.header("Authorization", bearerHeader(userToken)).buildDelete()
				.invoke();
		Assert.assertEquals(200, response.getStatus());

		// Provide the test user with admin privileges
		user.role = "user";
		userDao.saveUser(user);
	}

	@Test
	public void wrongPassword() {
		User alice = getTestUserAlice();

		// Change the password to trigger an authentication failure
		alice.password = "foo";

		// Attempt to get an auth token
		Response response = getAuthResponse(alice, application);
		Assert.assertEquals(401, response.getStatus());

		// Clean up
		releaseTestUser(alice);
	}

	@Test
	public void unsupportedGrantType() {
		User alice = getTestUserAlice();
		MultivaluedMap<String, String> authForm = getAuthForm(alice, application);
		authForm.putSingle("grant_type", "authorization_code"); // Set this to something other than "password"
		authForm.putSingle("redirect_uri", "http://test/test"); // To satisfy oltu validator
		authForm.putSingle("code", "x"); // To satisfy oltu validator
		
		// Should return a 400
		Response response = getTarget("/oauth2/token")
				.request()
				.header("Authorization",
						"Basic "
								+ Base64.encodeAsString(application.clientId
										+ ":" + application.secret))
				.buildPost(Entity.form(authForm)).invoke();
		Assert.assertEquals(400, response.getStatus());
		
		// Clean up
		releaseTestUser(alice);
	}
	
	@Test
	public void invalidGrantType() {
		User alice = getTestUserAlice();
		MultivaluedMap<String, String> authForm = getAuthForm(alice, application);
		authForm.putSingle("grant_type", "foobar"); // Incorrect grant type
		
		// Should return a 400
		Response response = getTarget("/oauth2/token")
				.request()
				.header("Authorization",
						"Basic "
								+ Base64.encodeAsString(application.clientId
										+ ":" + application.secret))
				.buildPost(Entity.form(authForm)).invoke();
		Assert.assertEquals(400, response.getStatus());
		
		// Clean up
		releaseTestUser(alice);
		
	}
}
