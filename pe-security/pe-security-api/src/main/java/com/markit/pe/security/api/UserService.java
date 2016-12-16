package com.markit.pe.security.api;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.markit.pe.commons.security.domain.User;
import com.markit.pe.security.exception.DuplicateUserException;
import com.markit.pe.security.exception.UserNotFoundException;

public interface UserService extends UserDetailsService {

    public void addUser(User user) throws DuplicateUserException;

    public User getUser(Long userId) throws UserNotFoundException;

    public User getUser(String username) throws UserNotFoundException;

    public void updateUser(User user) throws UserNotFoundException;

    public void deleteUser(Long userId) throws UserNotFoundException;

    public List<User> getUsers();
}
