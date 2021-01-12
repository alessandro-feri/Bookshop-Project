package com.feri.alessandro.attsw.project.services;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.feri.alessandro.attsw.project.model.User;
import com.feri.alessandro.attsw.project.repositories.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceWithMockitoTest {

	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	UserService userService;
	
	@Test
	public void test_getAllUsersWithZeroUsers() {
		when(userRepository.findAll()).thenReturn(Collections.emptyList());

		assertThat(userService.getAllUsers()).isEmpty();
		
		verify(userRepository, times(1)).findAll();
		verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void test_getAllUsersWithOneUser() {
		User testUser = new User(1L, "email", "username", "password");
		when(userRepository.findAll()).
				thenReturn(asList(testUser));
		
		assertThat(userService.getAllUsers()).containsExactly(testUser);
		
		verify(userRepository, times(1)).findAll();
		verifyNoMoreInteractions(userRepository);	
	}
	
	@Test
	public void test_gettAllUsersWithMoreThanOneUser() {
		User firstTestUser = new User(1L, "email1", "username1", "password1");
		User secondTestUser = new User(2L, "email2", "username2", "password2");
		
		when(userRepository.findAll()).thenReturn(asList(firstTestUser, secondTestUser));
		
		assertThat(userService.getAllUsers()).containsExactly(firstTestUser, secondTestUser);
		
		verify(userRepository, times(1)).findAll();
		verifyNoMoreInteractions(userRepository);
	}
	
	
	
}
