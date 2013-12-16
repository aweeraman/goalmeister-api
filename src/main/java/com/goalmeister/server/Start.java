package com.goalmeister.server;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.goalmeister.services.PingResource;

/**
 * Main class.
 * 
 */
public class Start {

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
	 * application.
	 * 
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer startServer(Configuration config) {

		// Load default values
		if (config == null) {
			config = Configuration.getInstance();
		}

		// create a resource config that scans for JAX-RS resources and
		// providers in com.goalmeister package
		final ResourceConfig rc = new ResourceConfig()
				.packages("com.goalmeister");

		// Register resources
		rc.register(PingResource.class);

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(
				URI.create(config.getBaseUri()), rc);

		// These two attributes should be configurable through the YAML file
		StaticHttpHandler httpHandler = new StaticHttpHandler(
				config.getHtmlDirectory());
		httpHandler.setFileCacheEnabled(config.isFileCacheEnabled());

		httpServer.getServerConfiguration().addHttpHandler(httpHandler, "/");

		return httpServer;
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		Configuration config = Configuration.getInstance();
		if (args.length > 0) {
			config = Configuration.loadConfiguration(args[0]);
		}
		
		final HttpServer server = startServer(config);
		
		System.out
				.println(String
						.format("Goalmeister API server started (%s/application.wadl)\nHit enter to stop it...",
								config.getBaseUri()));
		System.in.read();
		server.stop();
	}
}
