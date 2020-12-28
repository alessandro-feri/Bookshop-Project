package com.feri.alessandro.attsw.project.services;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;
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
	
	@Test
	public void test_getAllBooksWithMoreThanOneBook() {
		Book book1 = new Book(1L, "testBook1", "testType1", 10);
		Book book2 = new Book(2L, "testBook2", "testType2", 20);
		when(bookRepository.findAll()).thenReturn(asList(book1, book2));
		assertThat(bookService.getAllBooks()).containsExactly(book1, book2);
		verify(bookRepository, times(1)).findAll();
		verifyNoMoreInteractions(bookRepository);
		
	}
	
	@Test
	public void test_getBookById_found() {
		Book book = new Book(1L, "testBook", "testType", 10);
		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
		assertThat(bookService.getBookById(1)).isSameAs(book);
		verify(bookRepository, times(1)).findById(1L);
	}
	
	@Test
	public void test_getBookById_notFound() {
		when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThat(bookService.getBookById(1L)).isNull();
	}
	
	@Test
	public void test_insertNewBook() {
		Book bookToSave = new Book(2L, "bookToSave", "testTypeToSave", 0);
		Book bookSaved = new Book(1L, "testBookSaved", "testTypeSaved", 10);
		when(bookRepository.save(any(Book.class))).thenReturn(bookSaved);
		
		Book returnedBook = bookService.insertNewBook(bookToSave); 
		
		assertThat(returnedBook).isSameAs(bookSaved);
		
	}
	
	@Test
	public void test_deleteOneBook() {
		Book bookToDelete = new Book(1L, "bookToDelete", "testTypeToDelete", 10);
		bookService.deleteOneBook(bookToDelete);
		verify(bookRepository, times(1)).delete(bookToDelete);
		verifyNoMoreInteractions(bookRepository);
		
	}
	
	@Test
	public void test_deleteAllBooks() {
		bookService.deleteAllBooks();
		verify(bookRepository, times(1)).deleteAll();
		verifyNoMoreInteractions(bookRepository);

	}	
	
}
