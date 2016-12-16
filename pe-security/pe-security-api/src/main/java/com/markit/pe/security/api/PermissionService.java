package com.markit.pe.security.api;

import java.util.List;

import com.markit.pe.commons.security.domain.Permission;
import com.markit.pe.security.exception.DuplicatePermissionException;
import com.markit.pe.security.exception.PermissionNotFoundException;

public interface PermissionService {

    public void addPermission(Permission permission) throws DuplicatePermissionException;

    public Permission getPermission(Long id) throws PermissionNotFoundException;
    
    public Permission getPermission(String permissionname) throws PermissionNotFoundException;

    public void updatePermission(Permission permission) throws PermissionNotFoundException;

    public void deletePermission(Long id) throws PermissionNotFoundException;

    public List<Permission> getPermissions();

}
