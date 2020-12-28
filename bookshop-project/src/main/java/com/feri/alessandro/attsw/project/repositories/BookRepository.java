package com.feri.alessandro.attsw.project.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.feri.alessandro.attsw.project.model.Book;

/**
 * Temporary fake repository implementation.
 */
@Repository
public class BookRepository {
	
	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<Book> findAll() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
		
	}
	
	public Optional<Book> findById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Book save(Book book) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public void delete(Book book) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
		
	}
}