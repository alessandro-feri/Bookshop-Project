package com.feri.alessandro.attsw.project.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.feri.alessandro.attsw.project.model.User;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testJpaMapping() {
		User savedUser = entityManager.persistFlushFind(new User(null, "email", "username", "password"));
		assertThat(savedUser.getEmail()).isEqualTo("email");
		assertThat(savedUser.getUsername()).isEqualTo("username");
		assertThat(savedUser.getPassword()).isEqualTo("password");
		assertThat(savedUser.getId()).isNotNull();
		assertThat(savedUser.getId()).isGreaterThan(0);
		
		LoggerFactory.getLogger(UserRepositoryTest.class).info("Saved user " + savedUser.toString());
	}
	
	@Test
	public void test_findAllWithEmptyDatabase() {
		
		List<User> users = userRepository.findAll();
		
		assertThat(users).isEmpty();
	}
	
	@Test
	public void test_findAllUsingSave() {
		User userSaved = userRepository.save(new User(1L, "email", "username", "password"));
		
		List<User> users = userRepository.findAll();
		
		assertThat(users).containsExactly(userSaved);		
	}
	
	@Test
	public void test_findByUsername() {
		User user = entityManager.persistFlushFind(new User(null, "email", "username", "password"));
		
		Optional<User> found = userRepository.findByUsername("username");
		
		assertEquals(found.get(), user);
	}
	
	@Test
	public void test_findByEmail() {
		User user = entityManager.persistFlushFind(new User(null, "email", "username", "password"));
		
		Optional<User> found = userRepository.findByEmail("email");
		
		assertEquals(found.get(), user);
	}
	
	@Test
	public void test_findByUsernameAndPassword() {
		User user = new User(null, "email1@gmail", "username1", "samePassword");
		User notFound = new User(null, "email2@gmail", "username2", "samePassword");
		
		entityManager.persistFlushFind(user);
		entityManager.persistFlushFind(notFound);
		
		Optional<User> found = userRepository.findByUsernameAndPassword("username1", "samePassword");

		assertEquals(found.get(), user);
		assertNotEquals(found.get(), notFound);
	}

}
