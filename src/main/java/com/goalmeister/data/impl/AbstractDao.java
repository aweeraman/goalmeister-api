package com.goalmeister.data.impl;

import java.net.UnknownHostException;

import com.goalmeister.server.Configuration;
import com.mongodb.DB;
import com.mongodb.MongoClient;

public abstract class AbstractDao {
  private static Configuration config = Configuration.getInstance();

  protected static MongoClient client;
  protected static DB db;

  protected AbstractDao() {
    if (client == null) {
      client = getClient();
    }

    if (db == null) {
      db = getDb();
    }
  }

  public static MongoClient getClient() {
    MongoClient client;
    try {
      client = new MongoClient(config.getMongoHostname(), config.getMongoPort());
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
    return client;
  }

  public static DB getDb() {
    db = getClient().getDB(config.getMongoDb());
    db.authenticate(config.getMongoUsername(), config.getMongoPassword().toCharArray());
    return db;
  }
}
