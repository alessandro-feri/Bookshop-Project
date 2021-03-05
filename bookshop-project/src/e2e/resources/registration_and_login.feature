Feature: Registration and Login processes
		Specifications of authentication process

	Scenario: Registration process
  	Given I am on the Registration page
  	When I insert "my_email@gmail" into email field, "my_username" into username field and "my_password" into password field
  	And I click the "Register" button
  	Then I am on the "Result" page and "You have successfully registered!" is shown