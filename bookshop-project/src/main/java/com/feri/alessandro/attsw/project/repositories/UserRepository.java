package com.feri.alessandro.attsw.project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.feri.alessandro.attsw.project.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	@Query("Select u from User u where u.username = :username and u.password = :password")
	Optional<User> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}
