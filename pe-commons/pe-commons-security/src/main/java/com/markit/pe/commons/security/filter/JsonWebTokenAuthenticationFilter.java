package com.markit.pe.commons.security.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 
 * @author aditya.gupta2
 *
 */
@Component
public class JsonWebTokenAuthenticationFilter extends RequestHeaderAuthenticationFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonWebTokenAuthenticationFilter.class);

	public JsonWebTokenAuthenticationFilter() {
		// Don't throw exceptions if the header is missing
		LOGGER.info("Building the json web authentication filter");
		this.setExceptionIfHeaderMissing(false);
		LOGGER.info("Set the token in the Authorization Header");
		// This is the request header it will look for
		this.setPrincipalRequestHeader("Authorization");
	}

	@Override
	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}
}
