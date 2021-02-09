package com.feri.alessandro.attsw.project.services;

import org.springframework.stereotype.Service;

import com.feri.alessandro.attsw.project.exception.EmailExistException;
import com.feri.alessandro.attsw.project.exception.UsernameExistException;
import com.feri.alessandro.attsw.project.model.User;
import com.feri.alessandro.attsw.project.repositories.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;
	
	public User findUserByEmail(String email) throws EmailExistException {
		User exist = userRepository.findByEmail(email);
		
		if(exist != null) {
			throw new EmailExistException("There is already a user registered with the email provided."
					+ "Please, try with another email address.");
		}
		return exist;
	}

	public User findUserByUsername(String username) throws UsernameExistException {
		User exist = userRepository.findByUsername(username);
		
		if(exist != null) {
			throw new UsernameExistException("There is already a user registered with the username provided."
					+ "Please, try with another username.");
		}
		return exist;
	}

	public void saveUser(User user) {
		userRepository.save(user);
		
	}

}
