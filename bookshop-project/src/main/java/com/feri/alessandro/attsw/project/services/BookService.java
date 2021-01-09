package com.feri.alessandro.attsw.project.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.feri.alessandro.attsw.project.exception.BookNotFoundException;
import com.feri.alessandro.attsw.project.model.Book;

/**
 * 
 * Temporary fake implementation of service.
 */
@Service
public class BookService {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";
	
	public List<Book> getAllBooks() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Book getBookById(long id) throws BookNotFoundException {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Book insertNewBook(Book book) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Book editBookById(long id, Book testBook) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public void delete(Book toDelete) throws BookNotFoundException {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

}
