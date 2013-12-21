package com.goalmeister.data;

import java.util.List;

import com.goalmeister.model.User;
import com.goalmeister.model.UserToken;

public interface UserDao {

	public List<User> list();
	
	public User findUser(String email);
	
	public UserToken newSession(UserToken token);
	
	public UserToken findUserToken(String token);
	
	public UserToken findUserTokenByUsername(String username);
	
	public User newUser(User user);
	
	public void deleteUserById(String id);
	
	public void invalidateToken(String token);
	
	public void saveUser(User user);
	
}
