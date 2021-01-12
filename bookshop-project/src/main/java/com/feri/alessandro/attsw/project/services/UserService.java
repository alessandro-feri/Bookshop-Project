package com.feri.alessandro.attsw.project.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.feri.alessandro.attsw.project.exception.EmailExixstException;
import com.feri.alessandro.attsw.project.exception.UserNotFoundException;
import com.feri.alessandro.attsw.project.exception.UsernameExistException;
import com.feri.alessandro.attsw.project.model.User;
import com.feri.alessandro.attsw.project.repositories.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserById(Long id) throws UserNotFoundException {
		return userRepository.findById(id).
				orElseThrow(() -> new UserNotFoundException("User not found!"));
	}

	public User getUserByUsername(String username) throws UserNotFoundException {
		return userRepository.findByUsername(username).
				orElseThrow(() -> new UserNotFoundException("User not found!"));
	}

	public User getUserByEmail(String email) throws UserNotFoundException {
		return userRepository.findByEmail(email).
				orElseThrow(() -> new UserNotFoundException("User not found!"));
	}

	public User insertNewUser(User user) throws UsernameExistException, EmailExixstException {
		
		usernameVerification(user.getUsername());
		emailVerification(user.getEmail());
		
		user.setId(null);
		return userRepository.save(user);
	}

	private void usernameVerification(String username) throws UsernameExistException {
		if(userRepository.findByUsername(username).isPresent()){
			throw new UsernameExistException("There is an existent account with that username: " + username);
		}
	}
	
	private void emailVerification(String email) throws EmailExixstException {
		if(userRepository.findByEmail(email).isPresent()) {
			throw new EmailExixstException("There is an existent account with that email: " + email);
		}
		
	}

	
	
}
