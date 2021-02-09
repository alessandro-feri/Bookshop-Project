package com.feri.alessandro.attsw.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.feri.alessandro.attsw.project.exception.EmailExistException;
import com.feri.alessandro.attsw.project.exception.UsernameExistException;
import com.feri.alessandro.attsw.project.model.User;
import com.feri.alessandro.attsw.project.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
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
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEnabled(true);
		userRepository.save(user);
		
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		
		if(user != null) {
			return user;
		} else {
			throw new UsernameNotFoundException("username not found");
		}
	
	}

}
