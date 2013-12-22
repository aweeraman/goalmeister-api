package com.goalmeister.data;

import java.util.List;

import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;

public interface UserDao {

	List<User> list();

	User findUser(String email);

	UserToken newSession(UserToken token);

	UserToken findUserToken(String token);

	UserToken findUserTokenByUsername(String username);

	User newUser(User user);

	void deleteUserById(String id);

	void invalidateToken(String token);

	void saveUser(User user);

}
