package com.feri.alessandro.attsw.project.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
	
	
}
