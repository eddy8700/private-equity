package com.markit.pe.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.markit.pe.commons.security.domain.User;

/**
 * 
 * @author aditya.gupta2
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByUserEmail(String userEmail);

	
}
