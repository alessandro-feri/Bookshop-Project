package com.feri.alessandro.attsw.project.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.feri.alessandro.attsw.project.model.Book;
import com.feri.alessandro.attsw.project.repositories.BookRepository;

@Service
public class BookService {

	private BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}
	
	public Book getBookById(long id) {
		return bookRepository.findById(id).orElse(null);
	}

	public Book insertNewBook(Book book) {
		return bookRepository.save(book);
	}
	
	public Book editBookById(long id, Book replacementBook) {
		replacementBook.setId(id);
		return bookRepository.save(replacementBook);
	}	

	public void deleteOneBook(Book book) {
		bookRepository.delete(book);
		
	}

	public void deleteAllBooks() {
		bookRepository.deleteAll();
		
	}

}
