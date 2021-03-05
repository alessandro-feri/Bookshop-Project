package com.feri.alessandro.attsw.project.web;

import static com.gargoylesoftware.htmlunit.WebAssert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import com.feri.alessandro.attsw.project.exception.BookNotFoundException;
import com.feri.alessandro.attsw.project.exception.EmailExistException;
import com.feri.alessandro.attsw.project.exception.UsernameExistException;
import com.feri.alessandro.attsw.project.model.Book;
import com.feri.alessandro.attsw.project.services.BookService;
import com.feri.alessandro.attsw.project.services.UserService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

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
		
		HtmlPage result = form.getButtonByName("Register").click();
		
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
	
		HtmlPage result = form.getButtonByName("Register").click();
	
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
		
		HtmlPage result = form.getButtonByName("Register").click();
		
		assertThat(result.getTitleText()).isEqualTo("Result");
		assertThat(result.getBody().getTextContent().contains(
				"You have successfully registered!"));
		assertLinkPresentWithText(result, "Login Page");
		
	}
	
	
	@Test
	@WithMockUser
	public void test_HomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/");
		assertThat(page.getTitleText()).isEqualTo("Home");
	}

	@Test
	@WithMockUser
	public void test_HomePageStructure() throws Exception {
		HtmlPage page = webClient.getPage("/");
		
		assertThat(page.getAnchorByText("Insert").getHrefAttribute()).isEqualTo("/new");
		assertThat(page.getBody().getTextContent()).contains("There are no books");
		assertLinkNotPresentWithText(page, "Edit");
		assertLinkNotPresentWithText(page, "Delete");
		assertTextPresent(page, "Search");
		assertFormPresent(page, "search_form");
		assertInputPresent(page, "title_searched");
		assertElementPresent(page, "btn_search");
		assertFormPresent(page, "deleteAll");
		assertFormPresent(page, "Logout");

	}	
	
	@Test
	@WithMockUser
	public void test_homePage_WithNoBooks() throws Exception {
		when(bookService.getAllBooks()).thenReturn(Collections.emptyList());
		
		HtmlPage page = webClient.getPage("/");
		
		assertThat(page.getBody().getTextContent()).contains("There are no books");
	}
	
	@Test
	@WithMockUser
	public void test_homePageWithBooks_shouldShowThemInATable() throws Exception {
		List<Book> books = asList(
				new Book(1L, "title1", "type1", 10), new Book(2L, "title2", "type2", 15));
		
		when(bookService.getAllBooks()).thenReturn(books);
		
		HtmlPage page = webClient.getPage("/");
		
		assertThat(page.getBody().getTextContent()).doesNotContain("No books");
		HtmlTable table = page.getHtmlElementById("book_table");
		
		assertThat(table.asText()).isEqualTo(
				"ID	Title	Type	Price\n" + 
				"1	title1	type1	10	Edit	Delete\n" + 
				"2	title2	type2	15	Edit	Delete"
				
			);	
		
		page.getAnchorByHref("/edit/1");
		page.getAnchorByHref("/edit/2");
		page.getAnchorByHref("/delete?id=1");
		page.getAnchorByHref("/delete?id=2");
	}
	
	@Test
	public void test_Edit_And_New_PageStructure() throws Exception {
		when(bookService.getBookById(1L)).
		thenReturn(new Book(1L, "title", "type", 10));
	
		HtmlPage page = webClient.getPage("/edit/1");
		
		assertThat(page.getTitleText()).isEqualTo("Edit Page");
		assertTextPresent(page, "Edit Book");
		assertFormPresent(page, "book_form");
		assertElementPresent(page, "btn_save");
		assertTextPresent(page, "Title:");
		assertInputPresent(page, "title");
		assertTextPresent(page, "Type:");
		assertInputPresent(page, "type");
		assertTextPresent(page, "Price:");
		assertInputPresent(page, "price");
	}
	
	@Test
	@WithMockUser
	public void test_editWithNonExistentBook_shouldReturnBookNotFound () throws Exception {
		when(bookService.getBookById(1L)).thenThrow(BookNotFoundException.class);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		
		HtmlPage page = webClient.getPage("/edit/1");
		
		assertThat(page.getTitleText()).isEqualTo("Book not found!");
		
		assertThat(page.getBody().getTextContent().contains("Book not found!"));
	}
	
	@Test
	@WithMockUser
	public void test_editWithExistentBook() throws Exception {
		when(bookService.getBookById(1L)).
			thenReturn(new Book(1L, "title", "type", 10));
		
		HtmlPage page = webClient.getPage("/edit/1");
		
		final HtmlForm form = page.getFormByName("book_form");
		
		form.getInputByValue("title").setValueAttribute("modified_title");
		form.getInputByValue("type").setValueAttribute("modified_type");
		form.getInputByValue("10").setValueAttribute("15");
		
		form.getButtonByName("btn_submit").click();
		
		verify(bookService, times(1))
			.editBookById(1L, new Book(1L, "modified_title", "modified_type", 15));
	}
	
	@Test
	@WithMockUser
	public void test_editNewBook() throws Exception {
		HtmlPage page = webClient.getPage("/new");
		
		final HtmlForm form = page.getFormByName("book_form");
		
		form.getInputByName("title").setValueAttribute("new_title");
		form.getInputByName("type").setValueAttribute("new_type");
		form.getInputByName("price").setValueAttribute("10");
		
		form.getButtonByName("btn_submit").click();
		
		verify(bookService)
			.insertNewBook(new Book(null, "new_title", "new_type", 10));
	}
	
	@Test
	@WithMockUser
	public void test_searchView_withEmptyText_shouldShowErrorMessage() throws Exception {
		HtmlPage page = webClient.getPage("/");
		
		final HtmlForm form = page.getFormByName("search_form");
		
		form.getInputByName("title_searched").setValueAttribute("");
		
		HtmlPage search = form.getButtonByName("search_button").click();
		
		assertTextPresent(search, "Error! Please, insert a valid title.");
		assertThat(search.getAnchorByText("Home").getHrefAttribute()).isEqualTo("/");
		assertThat(search.getTextContent()).isNull();
	}
	
	@Test
	@WithMockUser
	public void test_searchView_whenBookNotFound() throws Exception {
		when(bookService.getBookByTitle(anyString())).thenThrow(BookNotFoundException.class);
		
		HtmlPage page = webClient.getPage("/");
		
		final HtmlForm form = page.getFormByName("search_form");
		
		form.getInputByName("title_searched").setValueAttribute("notFound");
		
		HtmlPage search = form.getButtonByName("search_button").click();
		
		assertThat(search.getTextContent()).isNull();
		
		assertTextPresent(search, "Book not found!");
		assertLinkPresentWithText(search, "Back to homepage");
	}
	
	@Test
	@WithMockUser
	public void test_searchView_WhenBookIsFound() throws Exception {
		Book found = new Book(1L, "test_title", "type", 10);
		when(bookService.getBookByTitle("test_title")).thenReturn(found);
		
		HtmlPage page = webClient.getPage("/");
		
		final HtmlForm form = page.getFormByName("search_form");
		
		form.getInputByName("title_searched").setValueAttribute("test_title");
		
		HtmlPage search = form.getButtonByName("search_button").click();
		
		search.getAnchorByHref("/");
		
		assertThat(search.getElementById("bookSearchedResult").getTextContent()).
			contains("Result", "Title", "Type", "Price", "test_title", "type", "10");
		
		assertThat(search.getAnchorByText("Home").getHrefAttribute()).isEqualTo("/");
		
		
		verify(bookService).getBookByTitle("test_title");
	}
	
	@Test
	@WithMockUser
	public void testDelete() throws Exception {
		Book book = new Book(1L, "title1", "type1", 10);
		List<Book> books = asList(book);
		
		when(bookService.getAllBooks()).thenReturn(books);
		when(bookService.getBookById(1L)).thenReturn(book);
		
		HtmlPage page = webClient.getPage("/");
		page.getAnchorByHref("/delete?id=1").click();
		
		verify(bookService).getBookById(1L);
		verify(bookService).deleteOneBook(book);
	}
	
	
	@Test
	@WithMockUser
	public void test_deleteAll() throws Exception {
		HtmlPage home = webClient.getPage("/");
		
		final HtmlForm form = home.getFormByName("deleteAll");
		
		form.getButtonByName("deleteAll_button").click();
		
		verify(bookService).deleteAllBooks();
	}
	
}