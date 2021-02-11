package com.feri.alessandro.attsw.project.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.feri.alessandro.attsw.project.model.User;

@Component
public interface UserRepository extends MongoRepository<User, String>{

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);

}
