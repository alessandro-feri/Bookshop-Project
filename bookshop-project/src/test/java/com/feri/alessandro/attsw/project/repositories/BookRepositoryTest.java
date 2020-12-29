package com.feri.alessandro.attsw.project.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
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
		assertThat(savedBook.getTitle()).isEqualTo("testBook");
		assertThat(savedBook.getType()).isEqualTo("testType");
		assertThat(savedBook.getPrice()).isEqualTo(10);
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getId()).isGreaterThan(0);
		assertThat(savedBook.getId()).isEqualTo(1);
		
		LoggerFactory.getLogger(BookRepositoryTest.class).info("Saved Book: " + savedBook.toString());
	}
	
	
	
}
