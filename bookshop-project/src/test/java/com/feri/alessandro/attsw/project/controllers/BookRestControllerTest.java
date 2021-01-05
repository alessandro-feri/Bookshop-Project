package com.feri.alessandro.attsw.project.controllers;

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
import org.springframework.http.MediaType;

import com.feri.alessandro.attsw.project.model.Book;
import com.feri.alessandro.attsw.project.services.BookService;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@RunWith(MockitoJUnitRunner.class)
public class BookRestControllerTest {

	@Mock
	private BookService bookService;
	
	@InjectMocks
	private BookRestController bookRestController;
	
	@Before
	public void setUp() {
		RestAssuredMockMvc.standaloneSetup(bookRestController);
	}
	
	@Test
	public void test_allBooksEmpty() {
		when(bookService.getAllBooks()).thenReturn(Collections.emptyList());
		
		given().
		when().
			get("/api/books").
		then().
			statusCode(200).
			assertThat().
				body(is(equalTo("[]"))
			);
		
		verify(bookService, times(1)).getAllBooks();
		verifyNoMoreInteractions(bookService);
	}
	
	@Test
	public void test_allBooksNotEmpty() {
		Book testBook1 = new Book(1L, "Il ritratto di Dorian Gray", "romanzo", 7);
		Book testBook2 = new Book(2L, "Harry Potter e la pietra filosofale", "romanzo", 9);
		when(bookService.getAllBooks()).thenReturn(asList(testBook1, testBook2));
		
		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
		when().
			get("/api/books").
		then().
			statusCode(200).
			assertThat().
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
	public void test_getBookByIdWithNonExistingBook() {
		when(bookService.getBookById(anyLong())).thenReturn(null);
		
		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
		when().
			get("api/books/1").
		then().
			statusCode(200).
			assertThat().
				body(is(equalTo(""))
			);
		
		verify(bookService, times(1)).getBookById(anyLong());
		verifyNoMoreInteractions(bookService);
	}
	
	@Test
	public void test_getBookByIdWithExistingBook() {
		when(bookService.getBookById(anyLong())).
				thenReturn(new Book(1L, "Il ritratto di Dorian Gray", "romanzo", 7));
		
		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
		when().
			get("api/books/1").
		then().
			statusCode(200).
			assertThat().
				body("id", equalTo(1),
					 "title", equalTo("Il ritratto di Dorian Gray"),
				     "type", equalTo("romanzo"),
				     "price", equalTo(7)
				);
		
		verify(bookService, times(1)).getBookById(1L);
		verifyNoMoreInteractions(bookService);
	}
	
	@Test
	public void test_insertNewBook() {
		Book testBook = new Book(null, "Il ritratto di Dorian Gray", "romanzo", 7);
		when(bookService.insertNewBook(testBook)).
			thenReturn(new Book(1L, "Il ritratto di Dorian Gray", "romanzo", 7));
		
		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			body(testBook).
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
		verify(bookService, times(1)).insertNewBook(testBook);
		verifyNoMoreInteractions(bookService);
	}
	
	

}
