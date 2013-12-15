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
	// TODO Base API URI the Grizzly HTTP server will listen on. This can be
	// overriden from the configuration filea
	public static final String BASE_API_URI = "http://localhost:8080/api";

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
	 * application.
	 * 
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer startServer() {
		// create a resource config that scans for JAX-RS resources and
		// providers in com.goalmeister package
		final ResourceConfig rc = new ResourceConfig()
				.packages("com.goalmeister");

		// Register resources
		rc.register(PingResource.class);

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(
				URI.create(BASE_API_URI), rc);

		// TODO: These two attributes should be configurable through the YAML file
		StaticHttpHandler httpHandler = new StaticHttpHandler("src/main/html/app");
		httpHandler.setFileCacheEnabled(false);

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
		final HttpServer server = startServer();
		System.out.println(String.format(
				"Jersey app started with WADL available at "
						+ "%sapplication.wadl\nHit enter to stop it...",
				BASE_API_URI));
		System.in.read();
		server.stop();
	}
}
