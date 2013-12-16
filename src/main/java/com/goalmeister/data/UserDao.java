package com.goalmeister.data;

import com.goalmeister.model.User;

public interface UserDao {

	public User findUser(String email);
		
}
