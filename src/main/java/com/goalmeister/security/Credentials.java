package com.goalmeister.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import com.goalmeister.model.User;

public class Credentials implements SecurityContext {

	private User user;
	private Principal principal;

	public Credentials(final User user) {
		this.user = user;
		this.principal = new Principal() {
			public String getName() {
				return user.email;
			}
		};
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}

	@Override
	public Principal getUserPrincipal() {
		return this.principal;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public boolean isUserInRole(String role) {
		return role.equals(user.role);
	}

}
