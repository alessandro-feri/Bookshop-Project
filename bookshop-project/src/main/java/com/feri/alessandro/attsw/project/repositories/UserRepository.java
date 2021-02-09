package com.feri.alessandro.attsw.project.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.feri.alessandro.attsw.project.model.User;

@Component
public interface UserRepository extends MongoRepository<User, String>{

	User findByEmail(String email);

	User findByUsername(String username);

}
