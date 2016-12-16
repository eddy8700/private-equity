package com.markit.pe.commons.security.authentication;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import com.markit.pe.commons.security.dto.JWTTokenBuilder;
import com.markit.pe.commons.security.utility.JsonWebTokenUtility;
/**
 * 
 * @author aditya.gupta2
 *
 */
@Component
public class JsonWebTokenAuthenticationProvider implements AuthenticationProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonWebTokenAuthenticationProvider.class);

	@Autowired
	private JsonWebTokenUtility tokenService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
			Authentication authenticatedUser = null;
		// Only process the PreAuthenticatedAuthenticationToken
			LOGGER.info("Authenticating the user for the http call");
		if (authentication.getClass().isAssignableFrom(PreAuthenticatedAuthenticationToken.class)
				&& authentication.getPrincipal() != null) {
			String tokenHeader = (String) authentication.getPrincipal();
			LOGGER.info("Token header present in the url",tokenHeader);
			UserDetails userDetails = parseToken(tokenHeader);
			if (userDetails != null) {
				authenticatedUser = new JsonWebTokenAuthentication(userDetails, tokenHeader);
			}
		} else {
			// It is already a JsonWebTokenAuthentication
			authenticatedUser = authentication;
		}
		LOGGER.info("Returning the authenticated user");
		return authenticatedUser;
	}

	private UserDetails parseToken(String tokenHeader) {
		UserDetails principal = null;
		JWTTokenBuilder jwtTokenBuilder = tokenService.parseAndValidate(tokenHeader);
		if (jwtTokenBuilder != null) {
			final Set<GrantedAuthority> authorities=jwtTokenBuilder.getRoles().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toSet());
			//fetch the permissions also
			final List<GrantedAuthority> permissions=jwtTokenBuilder.getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission)).collect(Collectors.toList());
			principal= new org.springframework.security.core.userdetails.User(jwtTokenBuilder.getUserName(), "", permissions);
			//principal = new User(jwtTokenBuilder.getUserName(), jwtTokenBuilder.getUserEmail(), authorities);
		}

		return principal;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(PreAuthenticatedAuthenticationToken.class)
				|| authentication.isAssignableFrom(JsonWebTokenAuthentication.class);
	}

}
