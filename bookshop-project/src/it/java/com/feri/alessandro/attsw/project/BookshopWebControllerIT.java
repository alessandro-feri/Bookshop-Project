package com.feri.alessandro.attsw.project;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.List;

import org.apache.bcel.generic.NEW;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.feri.alessandro.attsw.project.model.Book;
import com.feri.alessandro.attsw.project.model.User;
import com.feri.alessandro.attsw.project.repositories.BookRepository;
import com.feri.alessandro.attsw.project.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookshopWebControllerIT {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private BookRepository bookRepository;
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mvc;

	
	@Before
	public void setUp() {
		
		mvc = MockMvcBuilders.
				webAppContextSetup(context).
					apply(springSecurity()).
						build();
		
		userRepository.deleteAll();
		bookRepository.deleteAll();
		bookRepository.flush();
		
	}
	
	@After
	public void tearDown() {
		userRepository.deleteAll();
		bookRepository.deleteAll();
	}
	
	
	@Test
	public void test_returnLoginPageView() throws Exception {		
		ModelAndViewAssert.assertViewName(
				mvc.perform(get("/login")).
					andReturn().getModelAndView(), "login");
	}
	
	@Test
	public void test_returnRegiatrationPageView() throws Exception {
		ModelAndViewAssert.assertViewName(
				mvc.perform(get("/registration")).
					andReturn().getModelAndView(), "registration");
	}
	
	@Test
	public void test_createNewUser_shouldReturnResultPage() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(post("/saveUser")
				.param("email", "email@gmail")
				.param("username", "test")
				.param("password", "password")).andReturn().getModelAndView(), "registrationResult");
		
		assertEquals(userRepository.findAll().size(), 1);
		
		User saved = userRepository.findAll().get(0);
		
		assertEquals(saved.getEmail(), "email@gmail");
		assertEquals(saved.getUsername(), "test");
		assertThat(bCryptPasswordEncoder.matches("password", saved.getPassword()));
		
	}
	
	@Test
	public void test_createNewUser_WhenEmailAlreadyExistShouldNotSaveTheUser_andShouldReturnResultPage() throws Exception {
		User saved = new User();
		saved.setEmail("already_exist@gmail");
		saved.setUsername("username");
		saved.setPassword("password");
		
		userRepository.save(saved);
		
		ModelAndViewAssert.assertViewName(mvc.perform(post("/saveUser")
				.param("email", "already_exist@gmail")
				.param("username", "user")
				.param("password", "pass")).andReturn().getModelAndView(), "registrationResult");
		
		assertEquals(userRepository.findAll().size(), 1);
		assertThat(userRepository.findAll()).containsExactly(saved);
		
		
	}
	
	@Test
	public void test_createNewUser_WhenUsernamaAlreadyExistShouldNotSaveTheUser_andShouldReturnResultPage() throws Exception {
		User saved = new User();
		saved.setEmail("email@gmail");
		saved.setUsername("already_exist");
		saved.setPassword("password");
		
		userRepository.save(saved);
		
		ModelAndViewAssert.assertViewName(mvc.perform(post("/saveUser")
				.param("email", "not_exist@gmail")
				.param("username", "already_exist")
				.param("password", "pass")).andReturn().getModelAndView(), "registrationResult");
		
		assertEquals(userRepository.findAll().size(), 1);
		assertThat(userRepository.findAll()).containsExactly(saved);
	}
	
	@Test
	@WithMockUser
	public void test_returnHomePageView() throws Exception {
		ModelAndViewAssert.assertViewName(
				mvc.perform(get("/")).
					andReturn().getModelAndView(), "index");
	}
	
	@Test
	@WithMockUser
	public void test_homePage_shouldContainEmptyBookList() throws Exception {
		mvc.perform(get("/")).
			andExpect(view().name("index")).
			andExpect(model().attribute("books", Collections.emptyList())).
			andExpect(model().attribute("message", "No books!"));
		
		assertThat(bookRepository.findAll()).isEmpty();
	}
	
	@Test
	@WithMockUser
	public void test_homePage_shouldContainBooks() throws Exception {
		List<Book> books = asList(
				new Book(null, "title1", "type1", 10),
				new Book(null, "title2", "type2", 15),
				new Book(null, "title3", "type3", 20));
		
		bookRepository.saveAll(books);
	
		mvc.perform(get("/")).
			andExpect(view().name("index")).
			andExpect(model().attribute("books", books)).
			andExpect(model().attribute("message", ""));
		 
		assertThat(bookRepository.findAll().size()).isEqualTo(3);
			
	}
	
}
