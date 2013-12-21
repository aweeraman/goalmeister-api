package com.goalmeister;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.goalmeister.model.Application;

public class ApplicationCrudTest extends AbstractTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void crudTest() {
		Application app = applicationDao.create();
		Assert.assertNotNull(app);
		Assert.assertNotNull(app.clientId);
		Assert.assertNotNull(app.secret);
		
		applicationDao.deleteByClientId(app.clientId);
		
		Application newApp = applicationDao.findByClientId(app.clientId);
		Assert.assertNull(newApp);
	}
}
