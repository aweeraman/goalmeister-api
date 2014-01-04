package com.goalmeister.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.servlet.GrizzlyWebContainerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class.
 * 
 */
public class Start {
  
  private static final Logger logger = LoggerFactory.getLogger(Start.class);

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   * 
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer(Configuration config) throws IOException {

    // Load default values
    if (config == null) {
      config = Configuration.getInstance();
    }

    Map<String, String> initParams = new HashMap<String, String>();
    initParams.put("jersey.config.server.provider.packages",
        "com.goalmeister.services;com.goalmeister.security");
    initParams.put("com.sun.jersey.spi.container.ContainerRequestFilters",
        "com.goalmeister.security.OAuth2Filter");
    initParams.put("com.sun.jersey.spi.container.ContainerResponseFilters",
        "com.goalmeister.security.OAuth2Filter");

    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    HttpServer httpServer = GrizzlyWebContainerFactory.create(config.getBaseUri(), initParams);

    // Configure a static handler for serving static content
    StaticHttpHandler httpHandler = new StaticHttpHandler(config.getHtmlDirectory());
    httpHandler.setFileCacheEnabled(config.isFileCacheEnabled());
    httpServer.getServerConfiguration().addHttpHandler(httpHandler, "/");
    
    return httpServer;
  }

  /**
   * Main method.
   * 
   * @param args
   * @throws IOException
   * @throws InterruptedException 
   */
  public static void main(String[] args) throws IOException, InterruptedException {

    Configuration config = Configuration.getInstance();
    if (args.length > 0) {
      config = Configuration.loadConfiguration(args[0]);
    }

    final HttpServer server = startServer(config);

    logger.info("Starting API server: " + config.getBaseUri());
    System.out.println("Press CTRL^C to exit...");
    Thread.currentThread().join();
  }
}
