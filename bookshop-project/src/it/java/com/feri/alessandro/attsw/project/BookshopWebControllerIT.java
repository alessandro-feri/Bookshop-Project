package com.feri.alessandro.attsw.project;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.feri.alessandro.attsw.project.repositories.BookRepository;
import com.feri.alessandro.attsw.project.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookshopWebControllerIT {
	
	@Autowired
	private UserRepository userRepository;
	
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
	
	
}
