package com.feri.alessandro.attsw.project.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.feri.alessandro.attsw.project.model.Book;

@DataJpaTest
@RunWith(SpringRunner.class)
public class BookRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testJpaMapping() {
		Book savedBook = entityManager.persistFlushFind(new Book(null, "testBook", "testType", 10));
	}
	
}
