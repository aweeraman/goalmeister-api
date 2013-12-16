package com.goalmeister.data.mongo;

import org.mongojack.JacksonDBCollection;

import com.goalmeister.data.UserDao;
import com.goalmeister.model.User;
import com.mongodb.BasicDBObject;

public class UserDaoImpl extends AbstractDao implements UserDao {

	JacksonDBCollection<User, String> coll = JacksonDBCollection.wrap(
			db.getCollection("users"), User.class, String.class);
	
	@Override
	public User findUser(String email) {
		BasicDBObject obj = new BasicDBObject("email", email);
		return coll.findOne(obj);
	}
}
