package com.feri.alessandro.attsw.project.e2e.steps;

import static org.assertj.core.api.Assertions.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.feri.alessandro.attsw.project.repositories.UserRepository;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;

@SpringBootTest
public class BookshopSteps {
	
	private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
	
	private static String baseUrl = "http://localhost:" + port;
	
	private WebDriver webDriver;
	
	@Autowired
	UserRepository userRepository;
	
	@Before
	public void setUpWebDriver() {
		baseUrl = "http://localhost:" + port;
		webDriver = new ChromeDriver();
		userRepository.deleteAll();
	}
	
	@After
	public void closeBrowser() {
		webDriver.quit();
	}

	@Given("I am on the Registration page")
	public void i_am_on_the_Registration_page() {
	    webDriver.get(baseUrl + "/registration");
	}

	@When("I insert {string} into email field, {string} into username field and {string} into password field")
	public void i_insert_into_email_field_into_username_field_and_into_password_field(String email, String username, String password) {
	    webDriver.findElement(By.name("registration_form"));
	    
	    webDriver.findElement(By.name("email")).sendKeys(email);
	    webDriver.findElement(By.name("username")).sendKeys(username);
	    webDriver.findElement(By.name("password")).sendKeys(password);
	    
	}

	@When("I click the {string} button")
	public void i_click_the_button(String button) {
		webDriver.findElement(By.name(button)).click();
	}

	@Then("I am on the {string} page and {string} is shown")
	public void i_am_on_the_page_and_is_shown(String pageTitle, String message) {
	    assertThat(webDriver.getTitle()).isEqualTo(pageTitle);
	    assertThat(webDriver.getPageSource()).contains(message);
	}
}
