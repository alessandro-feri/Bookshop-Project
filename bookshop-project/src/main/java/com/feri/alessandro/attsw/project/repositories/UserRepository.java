package com.feri.alessandro.attsw.project.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.feri.alessandro.attsw.project.model.User;

public interface UserRepository extends MongoRepository<User, String>{

	User findByEmail(String email);

	User findByUsername(String username);

}
