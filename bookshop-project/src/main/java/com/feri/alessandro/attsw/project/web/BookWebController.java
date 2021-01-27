package com.feri.alessandro.attsw.project.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.feri.alessandro.attsw.project.model.Book;
import com.feri.alessandro.attsw.project.services.BookService;


@Controller
public class BookWebController {
	
	@Autowired
	private BookService bookService;
	
	@GetMapping("/")
	public String getIndex(Model model) {
		List<Book> allBooks = bookService.getAllBooks();
		model.addAttribute("books", allBooks);
		model.addAttribute("message", allBooks.isEmpty() ? "No books!" : "");
		
		return "index";
	}
	
}
