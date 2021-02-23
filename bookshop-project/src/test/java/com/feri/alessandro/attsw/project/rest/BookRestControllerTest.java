package com.feri.alessandro.attsw.project.rest;

import static java.util.Arrays.asList;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.anyLong;


import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.feri.alessandro.attsw.project.exception.BookExceptionHandler;
import com.feri.alessandro.attsw.project.exception.BookNotFoundException;
import com.feri.alessandro.attsw.project.model.Book;
import com.feri.alessandro.attsw.project.rest.BookRestController;
import com.feri.alessandro.attsw.project.services.BookService;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@RunWith(MockitoJUnitRunner.class)
public class BookRestControllerTest {

	private static final String BOOK_NOT_FOUND = "Book not found!";

	@Mock
	private BookService bookService;
	
	@InjectMocks
	private BookRestController bookRestController;
	
	/**
	 * Initializes BookExceptionController advice using the StaticApplicationContext with the single bean
	 * 
	 * @return HandlerExceptionResolver instantiated based on the BookExceptionController
	 * 
	 * So, my BookExceptionController is initialized using StaticApplicationContext and then I retrieve
	 * handlerExceptionResolver from it and pass it into RestAssuredMockMvc standaloneSetup()
	 */
	private HandlerExceptionResolver initBookExceptionHandlerResolvers() {
		StaticApplicationContext applicationContext = new StaticApplicationContext();
		applicationContext.registerSingleton("exceptionHandler", BookExceptionHandler.class);
		
		WebMvcConfigurationSupport webMvcConfigurationSupport = new WebMvcConfigurationSupport();
		webMvcConfigurationSupport.setApplicationContext(applicationContext);
		
		return webMvcConfigurationSupport.handlerExceptionResolver();
	}
	
	@Before
	public void setUp() {
		HandlerExceptionResolver handlerExceptionResolver = initBookExceptionHandlerResolvers();
		
		RestAssuredMockMvc.standaloneSetup(
				MockMvcBuilders
					.standaloneSetup(bookRestController)
					.setHandlerExceptionResolvers(handlerExceptionResolver)
		);
	}
	
	@Test
	public void testGET_allBooksEmpty() {
		when(bookService.getAllBooks()).thenReturn(Collections.emptyList());
		
		given().
		when().
			get("/api/books").
		then().
			statusCode(200).
			assertThat().
				contentType(MediaType.APPLICATION_JSON_VALUE).
				body(is(equalTo("[]"))
			);
		
		verify(bookService, times(1)).getAllBooks();
		verifyNoMoreInteractions(bookService);
	}
	
	@Test
	public void testGET_allBooksNotEmpty() {
		Book testBook1 = new Book(1L, "Il ritratto di Dorian Gray", "romanzo", 7);
		Book testBook2 = new Book(2L, "Harry Potter e la pietra filosofale", "romanzo", 9);
		when(bookService.getAllBooks()).thenReturn(asList(testBook1, testBook2));
		
		given().
		when().
			get("/api/books").
		then().
			statusCode(200).
			assertThat().
				contentType(MediaType.APPLICATION_JSON_VALUE).
				body("id[0]", equalTo(1),
					 "title[0]", equalTo("Il ritratto di Dorian Gray"),
					 "type[0]", equalTo("romanzo"),
					 "price[0]", equalTo(7),
					 "id[1]", equalTo(2),
					 "title[1]", equalTo("Harry Potter e la pietra filosofale"),
					 "type[1]", equalTo("romanzo"),
					 "price[1]", equalTo(9)
				);
		
		verify(bookService, times(1)).getAllBooks();
		verifyNoMoreInteractions(bookService);
	}
	
	@Test
	public void testGET_getBookById_WithNonExistingId() throws BookNotFoundException {
		when(bookService.getBookById(anyLong())).thenThrow(BookNotFoundException.class);
		
		given().
		when().
			get("api/books/id/1").
		then().
			statusCode(404).
			assertThat().
				body(is(equalTo(BOOK_NOT_FOUND))
			);
		
		verify(bookService, times(1)).getBookById(anyLong());
		verifyNoMoreInteractions(bookService);
	}
	
	@Test
	public void testGET_getBookById_WithExistingId() throws BookNotFoundException {
		when(bookService.getBookById(anyLong())).
				thenReturn(new Book(1L, "Il ritratto di Dorian Gray", "romanzo", 7));
		
		given().
		when().
			get("api/books/id/1").
		then().
			statusCode(200).
			assertThat().
				contentType(MediaType.APPLICATION_JSON_VALUE).
				body("id", equalTo(1),
					 "title", equalTo("Il ritratto di Dorian Gray"),
				     "type", equalTo("romanzo"),
				     "price", equalTo(7)
				);
		
		verify(bookService, times(1)).getBookById(1L);
		verifyNoMoreInteractions(bookService);
	}
	
	@Test
	public void test_getBookByTitle_WithNonExistingTitle() throws BookNotFoundException {
		when(bookService.getBookByTitle(anyString())).thenThrow(BookNotFoundException.class);
		
		given().
		when().
			get("api/books/title/testTitle").
		then().
			statusCode(404).
			assertThat().
				body(is(equalTo(BOOK_NOT_FOUND)));
		
		verify(bookService, times(1)).getBookByTitle(anyString());
		verifyNoMoreInteractions(bookService);
	}
	
	@Test
	public void test_getBookByTitle_WithExistingTitle() throws BookNotFoundException {
		when(bookService.getBookByTitle(anyString())).
			thenReturn(new Book(1L, "testTitle", "type", 10));
		
		given().
		when().
			get("api/books/title/testTitle").
		then().
			statusCode(200).
			assertThat().
			contentType(MediaType.APPLICATION_JSON_VALUE).
				body("id", equalTo(1),
					 "title", equalTo("testTitle"),
					 "type", equalTo("type"),
					 "price", equalTo(10)
				);
		
		verify(bookService, times(1)).getBookByTitle("testTitle");
		verifyNoMoreInteractions(bookService);
	}
	
	@Test
	public void testPOST_insertNewBook() {
		Book requesBodyBook = new Book(null, "Il ritratto di Dorian Gray", "romanzo", 7);
		when(bookService.insertNewBook(requesBodyBook)).
			thenReturn(new Book(1L, "Il ritratto di Dorian Gray", "romanzo", 7));
		
		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(requesBodyBook).
		when().
			post("api/books/new").
		then().
			statusCode(200).
			assertThat().
				body("id", equalTo(1),
					 "title", equalTo("Il ritratto di Dorian Gray"),
					 "type", equalTo("romanzo"),
					 "price", equalTo(7)
				);
		verify(bookService, times(1)).insertNewBook(requesBodyBook);
		verifyNoMoreInteractions(bookService);
	}
	
	@Test
	public void testPUT_editBookById_WithNonExistingId() throws BookNotFoundException {
		Book bookNotFound = new Book(null, "titleNotFound", "typeNotFound", 0);
		when(bookService.editBookById(1L, bookNotFound)).thenThrow(BookNotFoundException.class);
		
		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(bookNotFound).
		when().
			put("api/books/edit/1").
		then().
			statusCode(404).
			assertThat().
				body(is(equalTo(BOOK_NOT_FOUND)));
		
		//verify(bookService, times(1)).getBookById(1L);
		verify(bookService, times(1)).editBookById(1L, bookNotFound);	
	}
	
	@Test
	public void testPUT_editBookById_WithExistingId() throws BookNotFoundException {
		Book requestBodyBook = new Book(null, "testType", "testType", 10);
		when(bookService.editBookById(1L, requestBodyBook)).
			thenReturn(new Book(1L, "testBook", "testType", 10));
		
		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(requestBodyBook).
		when().
			put("api/books/edit/1").
		then().
			statusCode(200).
			assertThat().
			body("id", equalTo(1),
				 "title", equalTo("testBook"),
				 "type", equalTo("testType"),
				 "price", equalTo(10)
				);
		
		verify(bookService, times(1)).editBookById(1L, requestBodyBook);
		verifyNoMoreInteractions(bookService);
	}
	
	@Test
	public void testDELETE_deleteBookById_WithNonExistingId() throws BookNotFoundException {
		when(bookService.getBookById(anyLong())).thenThrow(BookNotFoundException.class);
		
		given().
		when().
			delete("api/books/delete/1").
		then().
			statusCode(404).
			assertThat().
				body(is(equalTo(BOOK_NOT_FOUND))
			);
		
		verify(bookService, times(1)).getBookById(1L);
	}
	
	@Test
	public void testDELETE_deleteBookById() throws BookNotFoundException {
		Book bookToDelete = new Book(1L, "testTitle", "testType", 10);
		when(bookService.getBookById(1L)).thenReturn(bookToDelete);
		
		given().
		when().
			delete("api/books/delete/1").
		then().
			statusCode(200);
		
		verify(bookService, times(1)).getBookById(1L);
		verify(bookService, times(1)).deleteOneBook(bookToDelete);
	}
	
	@Test
	public void testDELETE_deleteAllBooks() {
		
		given().
		when().
			delete("api/books/deleteAll").
		then().
			statusCode(200);
		
		verify(bookService, times(1)).deleteAllBooks();
	}

}
