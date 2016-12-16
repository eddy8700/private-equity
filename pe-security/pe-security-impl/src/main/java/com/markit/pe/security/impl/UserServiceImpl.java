package com.markit.pe.security.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.markit.pe.commons.security.domain.User;
import com.markit.pe.security.api.UserService;
import com.markit.pe.security.exception.DuplicateUserException;
import com.markit.pe.security.exception.UserNotFoundException;
import com.markit.pe.security.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
    @Autowired
    private UserRepository userRepo;

	@Override
	public void addUser(User user) throws DuplicateUserException {
		userRepo.save(user);
	}

    @Override
    public User getUser(Long userId) throws UserNotFoundException {
        return null;
    }

	@Override
	public User getUser(String username) throws UserNotFoundException {
		return null;
	}

	@Override
	public void updateUser(User user) throws UserNotFoundException {
		
	}

	@Override
	public void deleteUser(Long userId) throws UserNotFoundException {
	}

	@Override
	public List<User> getUsers() {
		return null;
	}
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return getUser(username);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
