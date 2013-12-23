package com.goalmeister.data.impl;

import java.util.LinkedList;
import java.util.List;

import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;

import com.goalmeister.data.UserDao;
import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;
import com.mongodb.BasicDBObject;

public class UserDaoImpl extends AbstractDao implements UserDao {

  private JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(
      db.getCollection("users"), User.class, String.class);

  private JacksonDBCollection<UserToken, String> user_tokens = JacksonDBCollection.wrap(
      db.getCollection("user_tokens"), UserToken.class, String.class);

  @Override
  public User findUser(String email) {
    BasicDBObject obj = new BasicDBObject("email", email);
    return users.findOne(obj);
  }

  @Override
  public UserToken newSession(UserToken token) {
    return user_tokens.save(token).getSavedObject();
  }

  @Override
  public UserToken findUserToken(String token) {
    BasicDBObject obj = new BasicDBObject("access_token", token);
    return user_tokens.findOne(obj);
  }

  @Override
  public User newUser(User user) {
    return users.save(user).getSavedObject();
  }

  @Override
  public void deleteUserById(String id) {
    users.removeById(id);
  }

  @Override
  public void invalidateToken(String token) {
    BasicDBObject obj = new BasicDBObject("access_token", token);
    user_tokens.remove(obj);
  }

  @Override
  public UserToken findUserTokenByUsername(String username) {
    BasicDBObject obj = new BasicDBObject("email", username);
    return user_tokens.findOne(obj);
  }

  @Override
  public void saveUser(User user) {
    users.save(user);
  }

  @Override
  public List<User> list() {
    DBCursor<User> cursor = users.find();
    List<User> userList = new LinkedList<User>();
    User user;
    while (cursor.hasNext()) {
      user = cursor.next();
      userList.add(user);
    }
    return userList;
  }

}
