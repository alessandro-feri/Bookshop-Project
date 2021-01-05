package com.feri.alessandro.attsw.project.services;

import java.util.List;

import org.springframework.stereotype.Service;

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

	public Book getBookById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Book insertNewBook(Book book) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

}
