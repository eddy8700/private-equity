/*package com.markit.pe.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.markit.pe.security.api.PermissionService;
import com.markit.pe.security.api.RoleService;
import com.markit.pe.security.api.UserService;

@Component
public class DataInitializer implements CommandLineRunner {
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;

	
	@Autowired
	private PermissionService permService;
	
	@Override
	public void run(String... arguments) throws Exception {
		// Create Roles
		Set<Permission> permissions= new HashSet<>();
		Set<Role> roles= new HashSet<>();
		Permission permission= new Permission();
		permission.setPermissionName("ADD_SECURITY");
		permissions.add(permission);
		Role role= new Role();
		role.setRoleName("ROLE_ADMIN");
		role.setPermissions(permissions);
		User user= new User();
		user.setUserEmail("aditya.gupta2@ihsmarkit.com");
		user.setUserName("aditya.gupta2");
		user.setRoles(roles);
		permService.addPermission(permission);
		roleService.addRole(role);
		userService.addUser(user);
		
		
		
	}
}
*/