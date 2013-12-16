package com.goalmeister.data.mongo;

import com.mongodb.MongoClient;

public abstract class AbstractDao {
	protected static MongoClient client;

	public AbstractDao() {
		if (client == null) {
			client = ConnectionFactory.getClient();
		}
	}
}
