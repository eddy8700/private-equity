package com.markit.pe.security.api;

import java.util.List;

import javax.management.relation.RoleNotFoundException;

import com.markit.pe.commons.security.domain.Role;
import com.markit.pe.security.exception.DuplicateRoleException;

public interface RoleService {

    public void addRole(Role role) throws DuplicateRoleException;

    public Role getRole(Long id) throws RoleNotFoundException;
    
    public Role getRole(String rolename) throws RoleNotFoundException;

    public void updateRole(Role role) throws RoleNotFoundException;

    public void deleteRole(Long id) throws RoleNotFoundException;

    public List<Role> getRoles();

}
