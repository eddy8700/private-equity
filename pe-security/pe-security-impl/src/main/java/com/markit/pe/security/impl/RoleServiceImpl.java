package com.markit.pe.security.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.markit.pe.commons.security.domain.Role;
import com.markit.pe.security.api.RoleService;
import com.markit.pe.security.exception.DuplicateRoleException;
import com.markit.pe.security.repository.RoleRepository;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    
    
@Autowired
private RoleRepository roleRepository;

    @Override
    public void addRole(Role role) throws DuplicateRoleException {
    	roleRepository.save(role);
    }

    @Override
    public Role getRole(Long id)  {
        return null;
    }

    @Override
    public Role getRole(String rolename)  {
    	return null;
    }

    @Override
    public void updateRole(Role role)  {
    }

    @Override
    public void deleteRole(Long id)  {
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
