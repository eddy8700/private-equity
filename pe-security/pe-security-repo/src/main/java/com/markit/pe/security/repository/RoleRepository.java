package com.markit.pe.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.markit.pe.commons.security.domain.Role;
/**
 * 
 * @author aditya.gupta2
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	List<Role> findByRoleName(String usersRole);
	
}
