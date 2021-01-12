package com.feri.alessandro.attsw.project.services;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.feri.alessandro.attsw.project.exception.UserNotFoundException;
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
	public void test_getAllUsersWithMoreThanOneUser() {
		User firstTestUser = new User(1L, "email1", "username1", "password1");
		User secondTestUser = new User(2L, "email2", "username2", "password2");
		
		when(userRepository.findAll()).thenReturn(asList(firstTestUser, secondTestUser));
		
		assertThat(userService.getAllUsers()).containsExactly(firstTestUser, secondTestUser);
		
		verify(userRepository, times(1)).findAll();
		verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void test_getUserById_found() throws UserNotFoundException {
		User testUser = new User(1L, "email", "username", "password");
		
		when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
		
		User result = userService.getUserById(1L);
		assertThat(result).isSameAs(testUser);
		
		verify(userRepository, times(1)).findById(1L);
		verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void test_getUserById_notFound_shouldThrowException() {
		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() ->
				userService.getUserById(1L)).
					isInstanceOf(UserNotFoundException.class).
						hasMessage("User not found!");
	}
	
	@Test
	public void test_getUserByUsername_found() throws UserNotFoundException {
		User testUser = new User(1L, "email", "testedUsername", "password");
		
		when(userRepository.findByUsername("testedUsername")).thenReturn(Optional.of(testUser));
		
		User result = userService.getUserByUsername("testedUsername");
		
		assertThat(result).isSameAs(testUser);
		
		verify(userRepository, times(1)).findByUsername("testedUsername");
		verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void test_getUserByUsername_notFound_shouldThrowException() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() ->
			userService.getUserByUsername("username")).
				isInstanceOf(UserNotFoundException.class).
					hasMessage("User not found!");
	}
	
	@Test
	public void test_getUserByEmail_found() throws UserNotFoundException {
		User testUser = new User(1L, "testedEmail", "username", "password");
		
		when(userRepository.findByEmail("testedEmail")).thenReturn(Optional.of(testUser));
		
		User result = userService.getUserByEmail("testedEmail");
		
		assertThat(result).isSameAs(testUser);
		
		verify(userRepository, times(1)).findByEmail("testedEmail");
		verifyNoMoreInteractions(userRepository);
	}
	
	@Test
	public void test_getUserByEmail_notFound_shouldThrowException() {
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> 
				userService.getUserByEmail("email")).
					isInstanceOf(UserNotFoundException.class).
						hasMessage("User not found!");
		
	}
}
