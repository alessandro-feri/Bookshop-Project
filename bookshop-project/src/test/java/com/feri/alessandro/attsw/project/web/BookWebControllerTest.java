package com.feri.alessandro.attsw.project.web;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import com.feri.alessandro.attsw.project.model.Book;
import com.feri.alessandro.attsw.project.services.BookService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = BookWebController.class)
public class BookWebControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private BookService bookService;
	
	@Test
	public void test_status200() throws Exception {
		mvc.perform(get("/")).
			andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void test_returnHomeView() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get("/")).
					andReturn().getModelAndView(), "index");
	}
	
	 @Test
	 public void test_HomeView_showsMessageWhenThereAreNoBooks() throws Exception {
		 when(bookService.getAllBooks()).thenReturn(Collections.emptyList());
		 
		 mvc.perform(get("/"))
		 	.andExpect(view().name("index"))
		 	.andExpect(model().attribute("books", Collections.emptyList()))
		 	.andExpect(model().attribute("message", "No books!"));
		 
		 verify(bookService, times(1)).getAllBooks();
		 verifyNoMoreInteractions(bookService);
	 }
	 
	 @Test
	 public void test_HomeView_showsBooks() throws Exception {
		 List<Book> books = asList(new Book(1L, "title", "type", 10));
		 
		 when(bookService.getAllBooks()).thenReturn(books);
		 
		 mvc.perform(get("/"))
		 	.andExpect(view().name("index"))
		 	.andExpect(model().attribute("books", books))
		 	.andExpect(model().attribute("message", ""));
		 
		 verify(bookService, times(1)).getAllBooks();
		 verifyNoMoreInteractions(bookService);	 
	 }
	 
}
