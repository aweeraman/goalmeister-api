package com.goalmeister;

import com.goalmeister.data.ApplicationDao;
import com.goalmeister.data.DaoFactory;
import com.goalmeister.data.UserDao;
import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;

public abstract class AbstractTest {

	protected UserDao userDao = DaoFactory.getUserDao();
	protected ApplicationDao applicationDao = DaoFactory.getApplicationDao();

	private static final String TEST_USER = "test@goalmeister.com";
	private static final String TEST_PASS = "password";

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

	public void removeTestUser(User user) {
		userDao.deleteUserById(user._id);
	}
	
	public UserToken getTemporaryAuthToken() {
		UserToken token = new UserToken();
		token.email = TEST_USER;
		token.access_token = "52af58b76ebccbae2923a400";
		return userDao.newSession(token);
	}
	
	public void removeTemporaryAuthToken(UserToken token) {
		userDao.invalidateToken(token.access_token);
	}
	
	public String authHeader(UserToken token) {
		return "Bearer " + token.access_token;
	}

}
