package com.markit.pe.commons.security.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class JsonWebTokenAuthentication extends AbstractAuthenticationToken {
	private static final long serialVersionUID = -6855809445272533821L;
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonWebTokenAuthentication.class);

	private UserDetails principal;
	private String jsonWebToken;

	public JsonWebTokenAuthentication(UserDetails principal, String jsonWebToken) {
		super(principal.getAuthorities());
		this.principal = principal;
		this.jsonWebToken = jsonWebToken;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return principal.getPassword();
	}

	public String getJsonWebToken() {
		return jsonWebToken;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}
	@Override
	public boolean isAuthenticated() {
		LOGGER.info("Is authenticated logic to be imlemented later as of now returning true");
		return true; //logic to be implemented later
	}

}
