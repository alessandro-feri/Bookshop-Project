package com.feri.alessandro.attsw.project.services;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
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

import com.feri.alessandro.attsw.project.exception.BookNotFoundException;
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
		when(bookRepository.findAll()).thenReturn(Collections.emptyList());
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
	public void test_getBookById_found() throws BookNotFoundException {
		Book book = new Book(1L, "testBook", "testType", 10);
		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
		assertThat(bookService.getBookById(1L)).isSameAs(book);
		verify(bookRepository, times(1)).findById(1L);
	}
	
	@Test
	public void test_getBookById_notFound_ShouldThrowsException() {
		when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThatThrownBy(() -> 
			bookService.getBookById(1L)).
				isInstanceOf(BookNotFoundException.class).hasMessage("Book not found!");
		
	}
	
	@Test
	public void test_insertNewBook() {
		Book bookToSave = new Book(2L, "testBookToSave", "testTypeToSave", 0);
		Book bookSaved = new Book(1L, "testBookSaved", "testTypeSaved", 10);
		when(bookRepository.save(any(Book.class))).thenReturn(bookSaved);
		
		Book returnedBook = bookService.insertNewBook(bookToSave); 
		
		assertThat(returnedBook).isSameAs(bookSaved);
		
		verify(bookRepository, times(1)).save(bookToSave);
		verifyNoMoreInteractions(bookRepository);

	}
	
	@Test
	public void test_editBookById_setsIdToArgument_and_ShouldReturnsSavedBook() {
		Book replacementBook = spy(new Book(null, "replacementBook", "replacementType", 5));
		Book replacedBook= new Book(1L, "replacedBook", "replacedType", 10);
		
		when(bookRepository.save(any(Book.class))).thenReturn(replacedBook);
		
		Book resultBook = bookService.editBookById(1L, replacementBook);
		
		assertThat(resultBook).isSameAs(replacedBook);
		
		verify(bookRepository, times(1)).save(replacementBook);
		verifyNoMoreInteractions(bookRepository);	
		
		InOrder inOrder = inOrder(replacementBook, bookRepository);
		inOrder.verify(replacementBook).setId(1L);
		inOrder.verify(bookRepository).save(replacementBook);
		
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
