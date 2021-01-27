package com.feri.alessandro.attsw.project.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class BookWebController {
	
	@GetMapping("/")
	public String getIndex(Model model) {
		return "index";
	}
	
}
