package com.goalmeister.management.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Goals service
 */
@Path("oauth2")
public class OAuth2Resource {
	
	@GET
	@Path("/token")
	public String token() {
		return "token";
	}
	
	@GET
	@Path("/invalidate_token")
	public String invalidate() {
		return "invalidate";
	}

}
