package com.feri.alessandro.attsw.project.repositories;

import static org.assertj.core.api.Assertions.assertThat;
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
	
	@Autowired
	private BookRepository bookRepository;
	
	@Test
	public void test_findBookByTitle() {
		Book bookToSave = new Book(null, "testTitle", "testType", 10);
		Book bookSaved = entityManager.persistFlushFind(bookToSave);
		
		Book bookFound = bookRepository.findByTitle("testTitle");
		assertThat(bookFound).isSameAs(bookSaved);
		
	}
		
}
