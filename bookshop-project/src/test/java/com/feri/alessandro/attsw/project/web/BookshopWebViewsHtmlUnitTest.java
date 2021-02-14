package com.feri.alessandro.attsw.project.web;

import static com.gargoylesoftware.htmlunit.WebAssert.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.feri.alessandro.attsw.project.services.BookService;
import com.feri.alessandro.attsw.project.services.UserService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = BookshopWebController.class)
public class BookshopWebViewsHtmlUnitTest {

	@Autowired
	private WebClient webClient;
	
	@MockBean
	private BookService bookService;
	
	@MockBean
	private UserService userService;

	
	@Test
	public void test_LoginPageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/login");
		assertThat(page.getTitleText()).isEqualTo("Login");
	}
	
	@Test
	public void test_LoginPageStructure() throws Exception {
		HtmlPage page = webClient.getPage("/login");
		
		assertFormPresent(page, "login_form");
		assertInputPresent(page, "email");
		assertInputPresent(page, "password");
		assertElementPresent(page, "login_button");
		assertElementPresent(page, "registration_button");
		assertLinkPresentWithText(page, "Register");
	}
}