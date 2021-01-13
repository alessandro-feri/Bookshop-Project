package com.feri.alessandro.attsw.project.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.feri.alessandro.attsw.project.model.User;

/**
 *
 *Temporary fake implementation of repository
 *
 */
@Repository
public class UserRepository {
	
	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<User> findAll() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Optional<User> findById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Optional<User> findByUsername(String username) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Optional<User> findByEmail(String email) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public User save(User user) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public void delete(User user) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
		
	}
}
