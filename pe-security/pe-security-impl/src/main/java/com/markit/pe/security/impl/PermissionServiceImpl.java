package com.markit.pe.security.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.markit.pe.commons.security.domain.Permission;
import com.markit.pe.security.api.PermissionService;
import com.markit.pe.security.exception.DuplicatePermissionException;
import com.markit.pe.security.exception.PermissionNotFoundException;
import com.markit.pe.security.repository.PermissionRepository;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
    static Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Autowired
    private PermissionRepository permissionRepository;
    
	@Override
	public void addPermission(Permission permission) throws DuplicatePermissionException {
		// TODO Auto-generated method stub
		permissionRepository.save(permission);
	}

	@Override
	public Permission getPermission(Long id) throws PermissionNotFoundException {
		// TODO Auto-generated method stub
		return permissionRepository.findOne(id);
	}

	@Override
	public Permission getPermission(String permissionname) throws PermissionNotFoundException {
		// TODO Auto-generated method stub
		return permissionRepository.findByPermissionName(permissionname);
	}

	@Override
	public void updatePermission(Permission permission) throws PermissionNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePermission(Long id) throws PermissionNotFoundException {

		
	}

	@Override
	public List<Permission> getPermissions() {
		// TODO Auto-generated method stub
		return permissionRepository.findAll();
	}
    

}
