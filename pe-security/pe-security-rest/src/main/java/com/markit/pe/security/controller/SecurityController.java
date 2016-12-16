package com.markit.pe.security.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.markit.pe.commons.security.domain.Role;
import com.markit.pe.commons.security.domain.User;
import com.markit.pe.commons.security.dto.AuthTokenDTO;
import com.markit.pe.commons.security.dto.AuthenticationTicketDTO;
import com.markit.pe.commons.security.dto.JWTTokenBuilder;
import com.markit.pe.commons.security.utility.JsonWebTokenUtility;
import com.markit.pe.security.repository.UserRepository;


/**
 * 
 * @author aditya.gupta2
 *
 */
@RestController
public class SecurityController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityController.class);
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JsonWebTokenUtility jsonWebTokenUtility;

	@RequestMapping(value = "/validateTicket", method = RequestMethod.POST)
	public AuthTokenDTO authenticate(@ModelAttribute  final AuthenticationTicketDTO authenticationTicketDTO) {
		// build user information
		// TODO some good way to call the mcs to validate the ticket instead of
		// injecting
		// their library some wrapper over mcs kind of
		final User user = userRepo.findByUserEmail("aditya.gupta2@ihsmarkit.com");
		LOGGER.info("Trying to authenticate ticket id",authenticationTicketDTO.getTicketId());
		final List<String> userRoles = user.getRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toList());
		List<String> userPermissions = new ArrayList<>();
		LOGGER.info("No of permissions for the user {} is {} ",user.getUsername(),userPermissions.size());
		for (Role userRole : user.getRoles()) {
			userPermissions = userRole.getPermissions().stream().map(r -> r.getPermissionName())
					.collect(Collectors.toList());
		}
		return buildJwtToken(user, userRoles, userPermissions);

	}

	private AuthTokenDTO buildJwtToken(final User user, final List<String> userRoles,
			final List<String> userPermissions) {
		// build the token
		final JWTTokenBuilder jwtTokenDTO = new JWTTokenBuilder(user.getUsername(), user.getUserEmail(), userRoles,
				userPermissions, buildExpirationTime());
		final String jwtToken = jsonWebTokenUtility.createJsonWebToken(jwtTokenDTO);
		final AuthTokenDTO authTokenDTO = new AuthTokenDTO(jwtToken);
		return authTokenDTO;
	}

	private Long buildExpirationTime() {
		//As of now token expiration time is current time + 1 min 
		return System.currentTimeMillis() + 600000;
	}
}
