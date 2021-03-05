package com.feri.alessandro.attsw.project.e2e;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/e2e/resources", monochrome = true)
public class RegistrationAndLoginE2E {

}
