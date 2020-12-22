package com.feri.alessandro.attsw.project.services;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.feri.alessandro.attsw.project.model.Book;
import com.feri.alessandro.attsw.project.repositories.BookRepository;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceWithMockitoTest {

	@Mock
	BookRepository bookRepository;
	
	@InjectMocks
	BookService bookService;
	
	@Test
	public void test_getAllBooksWithZeroBooks() {
		assertThat(bookService.getAllBooks()).isEmpty();
		verify(bookRepository, times(1)).findAll();
		verifyNoMoreInteractions(bookRepository);
		
	}
	
	@Test
	public void test_getAllBooksWithOneBook() {
		Book book = new Book(1L, "testBook", "testType", 10);
		when(bookRepository.findAll()).thenReturn(asList(book));
		assertThat(bookService.getAllBooks()).containsExactly(book);
		verify(bookRepository, times(1)).findAll();
		verifyNoMoreInteractions(bookRepository);
	}
	
	
	
}
