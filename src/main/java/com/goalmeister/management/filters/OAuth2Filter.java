package com.goalmeister.management.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

public class OAuth2Filter implements ContainerRequestFilter,
		ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext context,
			ContainerResponseContext response) throws IOException {
		System.out.println("-- filter --");
	}

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		System.out.println("-- filter --");
	}

}
