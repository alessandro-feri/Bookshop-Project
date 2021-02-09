package com.feri.alessandro.attsw.project.exception;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BookshopWebExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = BookNotFoundException.class)
	public String handleBookNotFoundException(Model model, HttpServletResponse response) {
		
		model.addAttribute("message", "Book not found!");
		response.setStatus(404);
		
		return "bookNotFound";
	}

}
