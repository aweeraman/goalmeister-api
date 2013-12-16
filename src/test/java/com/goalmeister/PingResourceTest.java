package com.goalmeister;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.goalmeister.server.Configuration;
import com.goalmeister.server.Start;

public class PingResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Start.startServer(null);
        // create the client
        Client c = ClientBuilder.newClient();
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
