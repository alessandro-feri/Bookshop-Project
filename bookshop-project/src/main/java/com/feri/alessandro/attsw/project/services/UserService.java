package com.feri.alessandro.attsw.project.services;

import org.springframework.stereotype.Service;

import com.feri.alessandro.attsw.project.model.User;
import com.feri.alessandro.attsw.project.repositories.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;
	
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public void saveUser(User user) {
		userRepository.save(user);
		
	}

}
