package com.markit.pe.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.markit.pe.commons.security.domain.Permission;
/**
 * 
 * @author aditya.gupta2
 *
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

	Permission findByPermissionName(String usersPermission);

}
