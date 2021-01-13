package com.feri.alessandro.attsw.project.services;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.feri.alessandro.attsw.project.exception.EmailExixstException;
import com.feri.alessandro.attsw.project.exception.UserNotFoundException;
import com.feri.alessandro.attsw.project.exception.UsernameExistException;
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
	
	@Test
	public void test_insertNewUser_setsIdToNull_and_returnsSavedUSer() throws UsernameExistException, EmailExixstException {
		User userToSave = spy(new User(2L, "emailToSave", "usernameToSave", "passwordToSave"));
		User savedUser = new User(1L, "savedEmail", "savedUsername", "savedPassword");
		
		when(userRepository.save(userToSave)).thenReturn(savedUser);
		
		User result = userService.insertNewUser(userToSave);
		
		assertThat(result).isSameAs(savedUser);
		
		verify(userRepository, times(1)).findByUsername("usernameToSave");
		verify(userRepository, times(1)).save(userToSave);
		
		InOrder inOrder = inOrder(userRepository, userToSave, userRepository);
		inOrder.verify(userRepository).findByUsername("usernameToSave");
		inOrder.verify(userToSave).setId(null);;
		inOrder.verify(userRepository).save(userToSave);
	}
	
	@Test
	public void test_insertNewUser_whenUsernameAlreadyExist_shouldThrowException() {
		User userToSave = new User(null, "email", "usernameAlreadyExist", "password");
		User userAlreadyExist = new User(1L, "email", "usernameAlreadyExist", "password");
		
		when(userRepository.findByUsername("usernameAlreadyExist")).thenReturn(Optional.of(userAlreadyExist));
		
		assertThatThrownBy(() -> 
				userService.insertNewUser(userToSave)).
					isInstanceOf(UsernameExistException.class).
						hasMessage("There is an existent account with that username: " + userAlreadyExist.getUsername());
		
		verify(userRepository, times(1)).findByUsername("usernameAlreadyExist");
	}
	
	@Test
	public void test_insertNewUser_whenEmailAlreadyExixst_shoulThrowException() {
		User userToSave = new User(null, "emailAlreadyExist", "username", "password");
		User userAlreadyExist = new User(1L, "emailAlreadyExist", "username", "password");
		
		when(userRepository.findByEmail("emailAlreadyExist")).thenReturn(Optional.of(userAlreadyExist));
		
		assertThatThrownBy(() -> 
				userService.insertNewUser(userToSave)).
					isInstanceOf(EmailExixstException.class).
						hasMessage("There is an existent account with that email: " + userAlreadyExist.getEmail());
		
		verify(userRepository, times(1)).findByEmail("emailAlreadyExist");
	}
	
	@Test
	public void test_editUserById_setsIdToArgument_and_ShouldReturnsSavedUser() throws UserNotFoundException {
		User replacementUser = spy(new User(null, "replacementEmail", "replacementUsername", "replacementPassword"));
		User replacedUser = new User(1L, "replacedEmail", "replacedUsername", "replacedPassword");
		
		when(userRepository.save(any(User.class))).thenReturn(replacedUser);
		when(userRepository.findById(1L)).thenReturn(Optional.of(replacedUser));
		
		User result = userService.editUserById(1L, replacementUser);
		
		assertThat(result).isSameAs(replacedUser);
		
		verify(userRepository, times(1)).findById(1L);
		verify(userRepository, times(1)).save(replacementUser);
		
		InOrder inOrder = inOrder(userRepository, replacementUser, userRepository);
		inOrder.verify(userRepository).findById(1L);
		inOrder.verify(replacementUser).setId(1L);
		inOrder.verify(userRepository).save(replacementUser);
				
	}
	
	@Test
	public void test_editUserById_notFound_shouldThrowException() {
		User user = new User(1L, "noFound", "notFound" , "notFound");
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() ->
				userService.editUserById(1L, user)).
					isInstanceOf(UserNotFoundException.class).
						hasMessage("User not found!");
		
		verify(userRepository, times(1)).findById(1L);
	}
	
	@Test
	public void test_deleteOneUser() {
		User toDelete = new User(1L, "emailToDelete", "usernameToDelete", "passwordToDelete");
		
		when(userRepository.findById(1L)).thenReturn(Optional.of(toDelete));
		
		assertThatCode(() -> userService.deleteOneUser(toDelete)).doesNotThrowAnyException();
		
		verify(userRepository, times(1)).findById(1L);
		verify(userRepository, times(1)).delete(toDelete);
	}
	
	@Test
	public void test_deleteOneUser_whenIdNotFound_shouldThrowException() {
		User userNotFound = new User(1L, "emailNotFound", "usernameNotFound", "passwordNotFound");
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> 
				userService.deleteOneUser(userNotFound)).
					isInstanceOf(UserNotFoundException.class).
						hasMessage("User not found!");
		
		verify(userRepository, times(1)).findById(1L);
	}
}
