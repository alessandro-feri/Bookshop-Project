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

	private static final String USER_NOT_FOUND = "User not found!";
	private UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserById(Long id) throws UserNotFoundException {
		return userRepository.findById(id).
				orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
	}

	public User getUserByUsername(String username) throws UserNotFoundException {
		return userRepository.findByUsername(username).
				orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
	}

	public User getUserByEmail(String email) throws UserNotFoundException {
		return userRepository.findByEmail(email).
				orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
	}

	public User insertNewUser(User user) throws UsernameExistException, EmailExixstException {
		
		usernameVerification(user.getUsername());
		emailVerification(user.getEmail());
		
		user.setId(null);
		return userRepository.save(user);
	}
	
	public User editUserById(Long id, User replacementUser) throws UserNotFoundException {
		sanityCheck(id);
		replacementUser.setId(id);
		return userRepository.save(replacementUser);
	}
	
	public void deleteOneUser(User user) throws UserNotFoundException {
		sanityCheck(user.getId());
		userRepository.delete(user);
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
	
	private void sanityCheck(Long id) throws UserNotFoundException {
		getUserById(id);
		
	}
	
}
