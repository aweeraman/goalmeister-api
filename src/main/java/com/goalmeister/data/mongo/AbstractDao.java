package com.goalmeister.data.mongo;

import java.net.UnknownHostException;

import com.goalmeister.server.Configuration;
import com.mongodb.DB;
import com.mongodb.MongoClient;

public abstract class AbstractDao {
	private static Configuration config = Configuration.getInstance();

	protected static MongoClient client;

	public AbstractDao() {
		if (client == null) {
			client = getClient();
		}
	}

	public static MongoClient getClient() {
		MongoClient client;
		try {
			client = new MongoClient(config.getMongoHostname(),
					config.getMongoPort());
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		return client;
	}

	public static DB getDb() {
		DB db = getClient().getDB(config.getMongoDb());
		db.authenticate(config.getMongoUsername(), config.getMongoPassword()
				.toCharArray());
		return db;
	}
}