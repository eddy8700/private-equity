package com.markit.pe.commons.security.utility;

import java.io.Serializable;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
/**
 * 
 * @author aditya.gupta2
 *
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomPermissionEvaluator.class);

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		LOGGER.info("Checking for the permission assigned to the user");
		 if ( authentication != null &&  permission instanceof String){
			   Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			   if(principal instanceof User){
				   Collection<GrantedAuthority> authorities=((User)principal).getAuthorities();
				   for (GrantedAuthority grantedAuthority : authorities) {
					   if(grantedAuthority.getAuthority().equals(permission)){
						   LOGGER.info("User has the permission {}",permission.toString());
						   return true; //user has the permission
					   }
					
				}
			   }
		 }
		 LOGGER.info("User does not have the permission {} to execute",permission.toString());
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		return false;
	}

}
