package com.goalmeister.data.mongo;

import java.util.UUID;

import org.mongojack.JacksonDBCollection;

import com.goalmeister.data.ApplicationDao;
import com.goalmeister.model.Application;
import com.mongodb.BasicDBObject;

public class ApplicationDaoImpl extends AbstractDao implements ApplicationDao {

	private JacksonDBCollection<Application, String> col_app = JacksonDBCollection
			.wrap(db.getCollection("application"), Application.class,
					String.class);

	@Override
	public Application findByClientId(String clientId) {
		BasicDBObject obj = new BasicDBObject("clientId", clientId);
		return col_app.findOne(obj);
	}

	@Override
	public void deleteByClientId(String clientId) {
		BasicDBObject obj = new BasicDBObject("clientId", clientId);
		col_app.remove(obj);
	}

	@Override
	public Application create() {
		Application application = new Application();
		application.clientId = UUID.randomUUID().toString();
		application.secret = UUID.randomUUID().toString();
		application.enabled = Boolean.TRUE;

		Application check = findByClientId(application.clientId);
		if (check != null) {
			// Rare case, should not happen. Return null to prevent possible
			// toe-stepping
			return null;
		}

		return col_app.save(application).getSavedObject();
	}

}
