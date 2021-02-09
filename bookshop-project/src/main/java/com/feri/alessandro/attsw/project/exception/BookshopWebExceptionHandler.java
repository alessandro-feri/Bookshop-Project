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
	
	@ExceptionHandler(value = EmailExistException.class)
	public String handleEmailExistException(Model model, HttpServletResponse response) {
		
		model.addAttribute("message", "There is already a user registered with the email provided."
				+ "Please, try with another email address.");
		response.setStatus(409);
		
		return "registrationResult";
	}

}
