package com.feri.alessandro.attsw.project.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.feri.alessandro.attsw.project.exception.BookNotFoundException;
import com.feri.alessandro.attsw.project.exception.EmailExistException;
import com.feri.alessandro.attsw.project.exception.UsernameExistException;
import com.feri.alessandro.attsw.project.model.Book;
import com.feri.alessandro.attsw.project.model.BookDTO;
import com.feri.alessandro.attsw.project.model.User;
import com.feri.alessandro.attsw.project.model.UserDTO;
import com.feri.alessandro.attsw.project.services.BookService;
import com.feri.alessandro.attsw.project.services.UserService;


@Controller
public class BookshopWebController {
	
	private static final String REDIRECT = "redirect:/";

	private static final String MESSAGE = "message";

	private static final String EMPTY_MESSAGE = "";

	@Autowired
	private BookService bookService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String getLoginPage() {
		return "login";
	}
	
	@GetMapping("/registration")
	public String getRegistrationPage() {
		return "registration";
	}
	
	@PostMapping("/saveUser")
	public String createNewUser(UserDTO userDTO, Model model) throws EmailExistException, UsernameExistException {
		User user = new User();
		user.setId(userDTO.getId());
		user.setEmail(userDTO.getEmail());
		user.setUsername(userDTO.getUsername());
		user.setPassword(userDTO.getPassword());
		
		userService.findUserByEmail(user.getEmail());
		userService.findUserByUsername(user.getUsername());
		
		model.addAttribute(MESSAGE, EMPTY_MESSAGE);
		userService.saveUser(user);
		
		return "registrationResult";
	}
	
	
	@GetMapping("/")
	public String getIndex(Model model) {
		List<Book> allBooks = bookService.getAllBooks();
		model.addAttribute("books", allBooks);
		model.addAttribute(MESSAGE, allBooks.isEmpty() ? "No books!" : EMPTY_MESSAGE);
		
		return "index";
	}

	@GetMapping("/edit/{id}")
	public String editBookById(@PathVariable Long id, Model model) throws BookNotFoundException {
		Book book = bookService.getBookById(id);
		model.addAttribute("book", book);
		model.addAttribute(MESSAGE, EMPTY_MESSAGE);
			
		return "edit";
	}
	
	@GetMapping("/new")
	public String newBook(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute(MESSAGE, EMPTY_MESSAGE);
		
		return "edit";
	}
	
	@PostMapping("/save")
	public String saveBook(BookDTO bookDTO) throws BookNotFoundException {
		Book book = new Book();
		book.setId(bookDTO.getId());
		book.setTitle(bookDTO.getTitle());
		book.setType(bookDTO.getType());
		book.setPrice(bookDTO.getPrice());
		
		Long id = book.getId();
		if(id == null) {
			bookService.insertNewBook(book);
		} else {
			bookService.editBookById(id, book);
		}
		
		return REDIRECT;
	}
	
	@GetMapping("/search")
	public String search (@RequestParam("title_searched") String title, Model model) throws BookNotFoundException {
		if(title.equals(EMPTY_MESSAGE)) {
			model.addAttribute(MESSAGE, "Error! Please, insert a valid title.");
		} else {
		
		Book book = bookService.getBookByTitle(title);
		model.addAttribute("book", book);
		model.addAttribute(MESSAGE, EMPTY_MESSAGE);
		
		}
		return "search";
	}
	
	@GetMapping("/delete")
	public String deleteBook(@RequestParam("id") Long id, Model model) throws BookNotFoundException {
		Book toDelete = bookService.getBookById(id);
		bookService.deleteOneBook(toDelete);
		
		return REDIRECT;
	}
	
	@GetMapping("/deleteAll")
	public String deleteAll() {
		bookService.deleteAllBooks();
		
		return REDIRECT;
	}
}
