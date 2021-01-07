package com.feri.alessandro.attsw.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.feri.alessandro.attsw.project.model.Book;
import com.feri.alessandro.attsw.project.services.BookService;

@RestController
public class BookRestController {

	@Autowired
	private BookService bookService;
	
	@GetMapping("/api/books")
	public List<Book> getAllBooks() {
		return bookService.getAllBooks();
	}
	
	@GetMapping("api/books/{id}")
	public Book getBookById(@PathVariable Long id) {
		return bookService.getBookById(id);
	}
	
	@PostMapping("/api/books/new")
	public Book insertNewBook(@RequestBody Book book) {
		return bookService.insertNewBook(book);
	}
	
	@PutMapping("/api/books/edit/{id}")
	public Book editBookById(@PathVariable Long id, @RequestBody Book replacementBook) {
		return bookService.editBookById(id, replacementBook);
	}
	
	@DeleteMapping("/api/books/delete/{id}")
	public void deleteBookById(@PathVariable Long id) {
		Book toDelete = bookService.getBookById(id);
		bookService.delete(toDelete);
	}
	
}
