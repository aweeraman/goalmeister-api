package com.goalmeister.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Configuration {

	private static Configuration config = new Configuration();
	private static Map<String, Object> configItems = new HashMap<String, Object>();

	private static final String BASE_URI_KEY = "base_uri";
	private static final String HTML_DIR_KEY = "html_dir";
	private static final String CACHING_KEY = "file_cache_enabled";

	private Configuration() {
	}

	public static synchronized Configuration loadConfiguration(String yamlFile)
			throws IOException {
		InputStream is = new FileInputStream(new File(yamlFile));
		configItems = (Map<String, Object>) new Yaml().load(is);
		return config;
	}

	public static Configuration getInstance() {
		return config;
	}

	public String getStringKey(String keyName, String defaultValue) {
		String keyValue = (String) configItems.get(keyName);
		if (keyValue == null) {
			return defaultValue;
		}
		return keyValue;
	}

	public Boolean getBooleanKey(String keyName, Boolean defaultValue) {
		Boolean keyValue = (Boolean) configItems.get(keyName);
		if (keyValue == null) {
			return defaultValue;
		}
		return keyValue;
	}

	public String getBaseUri() {
		return getStringKey(BASE_URI_KEY, "http://localhost:8080/api");
	}

	public String getHtmlDirectory() {
		return getStringKey(HTML_DIR_KEY, "src/main/html/app");
	}

	public boolean isFileCacheEnabled() {
		return getBooleanKey(CACHING_KEY, Boolean.FALSE).booleanValue();
	}
}
