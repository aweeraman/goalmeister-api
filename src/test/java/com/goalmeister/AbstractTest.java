package com.goalmeister;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.goalmeister.data.ApplicationDao;
import com.goalmeister.data.DaoFactory;
import com.goalmeister.data.UserDao;
import com.goalmeister.model.Application;
import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;
import com.goalmeister.server.Configuration;
import com.goalmeister.server.Start;

public abstract class AbstractTest {

	private HttpServer server;
	private ClientConfig clientConfig = new ClientConfig()
			.register(new JacksonFeature());
	private Client client = ClientBuilder.newClient(clientConfig);

	protected UserDao userDao = DaoFactory.getUserDao();
	protected ApplicationDao applicationDao = DaoFactory.getApplicationDao();

	private static final String TEST_USER = "test@goalmeister.com";
	private static final String TEST_PASS = "password";

	public void startServer() {
		try {
			server = Start.startServer(null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void stopServer() {
		if (server != null) {
			server.shutdownNow();
		}
	}

	public Application getApplication() {
		return applicationDao.create();
	}

	public void releaseApplication(Application application) {
		applicationDao.deleteByClientId(application.clientId);
	}

	public User getTestUser() {
		User user = userDao.findUser(TEST_USER);
		if (user == null) {
			user = new User();
			user.email = TEST_USER;
			user.password = TEST_PASS;
			user = userDao.newUser(user);
		}
		return user;
	}

	public Response responseForPost(String path, Object object,
			UserToken userToken) {
		return getTarget(path).request()
				.header("Authorization", bearerHeader(userToken))
				.accept("application/json")
				.buildPost(Entity.entity(object, "application/json")).invoke();
	}

	public Object objectForPost(String path, Object object,
			UserToken userToken, Class clazz) {
		return (Object) getTarget(path).request()
				.header("Authorization", bearerHeader(userToken))
				.accept("application/json")
				.buildPost(Entity.entity(object, "application/json"))
				.invoke(clazz);
	}

	public MultivaluedMap<String, String> getAuthForm(User user,
			Application application) {
		MultivaluedMap<String, String> map = new MultivaluedHashMap<String, String>();
		map.putSingle("grant_type", "password");
		map.putSingle("username", user.email);
		map.putSingle("password", user.password);
		map.putSingle("client_id", application.clientId);
		map.putSingle("client_secret", application.secret);
		return map;
	}

	public UserToken getAuthToken(User user, Application application) {
		return getTarget("/oauth2/token")
				.request()
				.header("Authorization",
						"Basic "
								+ Base64.encodeAsString(application.clientId
										+ ":" + application.secret))
				.buildPost(Entity.form(getAuthForm(user, application)))
				.invoke(UserToken.class);
	}

	public Response getAuthResponse(User user, Application application) {
		return getTarget("/oauth2/token")
				.request()
				.header("Authorization",
						"Basic "
								+ Base64.encodeAsString(application.clientId
										+ ":" + application.secret))
				.buildPost(Entity.form(getAuthForm(user, application)))
				.invoke();
	}

	public Response invalidateToken(UserToken userToken) {
		return getTarget("/oauth2/invalidate_token").request()
				.header("Authorization", bearerHeader(userToken)).get();
	}

	public WebTarget getTarget(String path) {
		return client.target(Configuration.getInstance().getBaseUri() + path);
	}

	public void releaseTestUser(User user) {
		userDao.deleteUserById(user._id);
	}

	public UserToken getAuthToken() {
		UserToken token = new UserToken();
		token.email = TEST_USER;
		token.access_token = "52af58b76ebccbae2923a400";
		return userDao.newSession(token);
	}

	public void releaseAuthToken(UserToken token) {
		userDao.invalidateToken(token.access_token);
	}

	public String bearerHeader(UserToken token) {
		return "Bearer " + token.access_token;
	}

}
