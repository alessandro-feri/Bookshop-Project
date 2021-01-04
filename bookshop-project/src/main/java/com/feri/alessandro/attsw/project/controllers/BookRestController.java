package com.feri.alessandro.attsw.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
