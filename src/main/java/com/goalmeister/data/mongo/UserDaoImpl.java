package com.goalmeister.data.mongo;

import org.mongojack.JacksonDBCollection;

import com.goalmeister.data.UserDao;
import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;
import com.mongodb.BasicDBObject;

public class UserDaoImpl extends AbstractDao implements UserDao {

	private JacksonDBCollection<User, String> col_user = JacksonDBCollection.wrap(
			db.getCollection("users"), User.class, String.class);
	
	private JacksonDBCollection<UserToken, String> col_token = JacksonDBCollection.wrap(
			db.getCollection("user_tokens"), UserToken.class, String.class);
	
	@Override
	public User findUser(String email) {
		BasicDBObject obj = new BasicDBObject("email", email);
		return col_user.findOne(obj);
	}

	@Override
	public UserToken newSession(UserToken token) {
		return col_token.save(token).getSavedObject();
	}

	@Override
	public UserToken findUserToken(String token) {
		BasicDBObject obj = new BasicDBObject("token", token);
		return col_token.findOne(obj);
	}

	@Override
	public User newUser(User user) {
		return col_user.save(user).getSavedObject();
	}

	@Override
	public void deleteUserById(String id) {
		col_user.removeById(id);
	}

	@Override
	public void invalidateToken(String token) {
		BasicDBObject obj = new BasicDBObject("token", token);
		col_token.remove(obj);
	}

	@Override
	public UserToken findUserTokenByUsername(String username) {
		BasicDBObject obj = new BasicDBObject("email", username);
		return col_token.findOne(obj);
	}

}
