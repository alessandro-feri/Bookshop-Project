package com.feri.alessandro.attsw.project;

import static org.junit.Assert.*;

import org.junit.Test;

import com.feri.alessandro.attsw.project.model.Book;
import com.feri.alessandro.attsw.project.model.User;

/**
 * A temporary test just to make sure jacoco.xml is generated and a report can
 * be sent to Coveralls.
 * 
 * This must be removed when we have at least a test executed by surefire.
 */
public class TemporaryTest {

	@Test
	public void test() {
		assertNotNull(new Book(1L, "book", "type", 0));
		assertNotNull(new User(1L, "email", "username", "password"));
	}

}