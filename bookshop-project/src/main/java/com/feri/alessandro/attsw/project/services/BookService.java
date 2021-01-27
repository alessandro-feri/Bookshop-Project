package com.feri.alessandro.attsw.project.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.feri.alessandro.attsw.project.model.Book;

@Service
public class BookService {

	private static final String TEMPORARY_IMPLEMENTATION = "temporary_implementation";

	public List<Book> getAllBooks() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

}
