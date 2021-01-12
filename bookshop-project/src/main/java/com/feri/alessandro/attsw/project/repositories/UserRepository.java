package com.feri.alessandro.attsw.project.repositories;

import java.util.List;

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
	

	
}
