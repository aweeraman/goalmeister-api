package com.goalmeister;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.goalmeister.model.Application;
import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;

public class PingTest extends AbstractTest {

	private User user;
	private UserToken userToken;
	private Application application;

	@Before
	public void setUp() throws Exception {
		startServer();
		user = getTestUser();
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

	/**
	 * Test to see that the message "Ping!" is sent in the response.
	 */
	@Test
	public void ping() {
		// Once authenticated the ping request should be successful
		String responseMsg = getTarget("/ping").request()
				.header("Authorization", bearerHeader(userToken))
				.get(String.class);
		Assert.assertEquals("Ping!", responseMsg);
	}

	@Test
	public void rejectAuthTokenRequestIfApplicationDisabled() {

		// Disable the application in the backend
		application.enabled = Boolean.FALSE;
		applicationDao.update(application);

		// Attempting to authenticate should return a 403
		Response forbiddenResponse = getAuthResponse(user, application);
		Assert.assertEquals(forbiddenResponse.getStatus(),403);
	}

	@Test
	public void checkAuthTokenInvalidation() {
		// Invalidate the token and confirm if 200 is received
		Response response = invalidateToken(userToken);
		Assert.assertEquals(response.getStatus(), 200);

		// Subsequent calls to protected endpoints with same token should
		// return a 403
		response = getTarget("/ping").request()
				.header("Authorization", bearerHeader(userToken)).get();
		Assert.assertEquals(response.getStatus(), 403);
	}
}
