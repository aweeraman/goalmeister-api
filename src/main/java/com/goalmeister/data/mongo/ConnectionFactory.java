package com.goalmeister.data.mongo;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;

public class ConnectionFactory {

	public static MongoClient getClient() {
		MongoClient client;
		try {
			client = new MongoClient();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		System.out.println("Connected to " + client.getConnectPoint());
		return client;
	}
}
