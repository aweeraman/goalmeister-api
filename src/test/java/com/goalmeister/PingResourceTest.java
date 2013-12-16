package com.goalmeister;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.goalmeister.server.Configuration;
import com.goalmeister.server.Start;

import static org.junit.Assert.assertEquals;

public class PingResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Start.startServer(null);
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Configuration.getInstance().getBaseUri());
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    /**
     * Test to see that the message "Ping!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        String responseMsg = target.path("ping").request().get(String.class);
        assertEquals("Ping!", responseMsg);
    }
}
