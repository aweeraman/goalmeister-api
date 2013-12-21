package com.goalmeister;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.goalmeister.server.Configuration;

public class ConfigurationTest {
	
	Configuration config = null;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConfiguration() throws IOException {
		config = Configuration.getInstance();
		
		assertEquals("Default base URI", "http://localhost:8080/api", config.getBaseUri());
		assertEquals("Default html directory", "src/main/html/app", config.getHtmlDirectory());
		assertEquals("File caching enabled", false, config.isFileCacheEnabled());

		config = Configuration.loadConfiguration("config.yaml");

		assertEquals("Default base URI", "http://localhost:8080/api", config.getBaseUri());
		assertEquals("Default html directory", "src/main/html/app", config.getHtmlDirectory());
		assertEquals("File caching enabled", false, config.isFileCacheEnabled());
	}
}
