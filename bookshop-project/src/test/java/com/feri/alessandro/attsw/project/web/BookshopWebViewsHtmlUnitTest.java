package com.feri.alessandro.attsw.project.web;

import static com.gargoylesoftware.htmlunit.WebAssert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.feri.alessandro.attsw.project.exception.EmailExistException;
import com.feri.alessandro.attsw.project.exception.UsernameExistException;
import com.feri.alessandro.attsw.project.services.BookService;
import com.feri.alessandro.attsw.project.services.UserService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
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
	
	@Test
	public void test_RegistrationPageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/registration");
		assertThat(page.getTitleText()).isEqualTo("Registration Page");
	}
	
	@Test
	public void test_RegistrationPageStructure() throws Exception {
		HtmlPage page = webClient.getPage("/registration");
		
		assertFormPresent(page, "registration_form");
		assertInputPresent(page, "email");
		assertInputPresent(page, "username");
		assertInputPresent(page, "password");
		assertTextPresent(page, "or back to");
		assertElementPresent(page, "login_button");
		assertElementPresent(page, "registration_button");
		assertLinkPresentWithText(page, "Login");
		
	}
	
	@Test
	public void test_RegistrationWhenEmailAlreadyExist() throws Exception {
		when(userService.findUserByEmail("email_exist@gmail")).
			thenThrow(EmailExistException.class);
		
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		
		HtmlPage page = webClient.getPage("/registration");
		
		HtmlForm form = page.getFormByName("registration_form");
		
		form.getInputByName("email").setValueAttribute("email_exist@gmail");
		form.getInputByName("username").setValueAttribute("username");
		form.getInputByName("password").setValueAttribute("password");
		
		HtmlPage result = form.getButtonByName("reg_button").click();
		
		assertThat(result.getTitleText()).isEqualTo("Result");
		assertLinkPresentWithText(result, "Go back to Registration page");
		assertThat(result.getBody().getTextContent().contains(
				"There is already a user registered with the email provided."
				+ "Please, try with another email address."));
		
		verify(userService).findUserByEmail("email_exist@gmail");
		
	}
	
	@Test
	public void test_RegistrationWhenUsernameAlreadyExist() throws Exception {
		when(userService.findUserByUsername("username_exist")).
			thenThrow(UsernameExistException.class);
	
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	
		HtmlPage page = webClient.getPage("/registration");
	
		HtmlForm form = page.getFormByName("registration_form");
	
		form.getInputByName("email").setValueAttribute("email@gmail");
		form.getInputByName("username").setValueAttribute("username_exist");
		form.getInputByName("password").setValueAttribute("password");
	
		HtmlPage result = form.getButtonByName("reg_button").click();
	
		assertThat(result.getTitleText()).isEqualTo("Result");
		assertLinkPresentWithText(result, "Go back to Registration page");
		assertThat(result.getBody().getTextContent().contains(
			"There is already a user registered with the username provided."
			+ "Please, try with another username."));
	
		verify(userService).findUserByUsername("username_exist");
	}
	
	@Test
	public void test_SuccessfullRegistration() throws Exception {
		HtmlPage page = webClient.getPage("/registration");
		
		HtmlForm form = page.getFormByName("registration_form");
		
		form.getInputByName("email").setValueAttribute("email@gmail");
		form.getInputByName("username").setValueAttribute("username");
		form.getInputByName("password").setValueAttribute("password");
		
		HtmlPage result = form.getButtonByName("reg_button").click();
		
		assertThat(result.getTitleText()).isEqualTo("Result");
		assertThat(result.getBody().getTextContent().contains(
				"You have been registered successfully!"));
		assertLinkPresentWithText(result, "Login Page");
		
	}
	
}