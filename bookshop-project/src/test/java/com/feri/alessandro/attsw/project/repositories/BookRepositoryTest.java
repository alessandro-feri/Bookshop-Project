package com.feri.alessandro.attsw.project.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Optional;

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
	
	@Autowired
	private BookRepository bookRepository;
	
	@Test
	public void testJpaMapping() {
		Book savedBook = entityManager.persistFlushFind(new Book(null, "testBook", "testType", 10));
		assertThat(savedBook.getTitle()).isEqualTo("testBook");
		assertThat(savedBook.getType()).isEqualTo("testType");
		assertThat(savedBook.getPrice()).isEqualTo(10);
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getId()).isGreaterThan(0);
		
		LoggerFactory.getLogger(BookRepositoryTest.class).info("Saved Book: " + savedBook.toString());
	}
	
	@Test
	public void test_findAllUsingSave() {
		Book bookSaved = bookRepository.save(new Book(null, "testBook", "testType", 10));
		Collection<Book> books = bookRepository.findAll();
		assertThat(books).containsExactly(bookSaved);
	}
	
	@Test
	public void test_findAllUsingPersistFlushFind() {
		Book bookSaved = entityManager.persistFlushFind(new Book(null, "testBook", "testType", 10));
		Collection<Book> books = bookRepository.findAll();
		assertThat(books).containsExactly(bookSaved);
	}
	
	@Test
	public void test_deleteUsingJPARepositoryMethod() {
		Book bookSaved1 = entityManager.persistFlushFind(new Book(null, "testBook", "testType", 10));
		Book bookSaved2 = entityManager.persistFlushFind(new Book(null, "testBook", "testType", 10));
		assertThat(bookRepository.findAll()).containsExactly(bookSaved1, bookSaved2);
		
		bookRepository.delete(bookSaved1);
		
		Collection<Book> bookListAfterDelete = bookRepository.findAll();
		
		assertThat(bookListAfterDelete).containsExactly(bookSaved2);
		
	}
	
	@Test
	public void test_deleteAllUsingJPARepositoryMethod() {
		Book bookSaved1 = bookRepository.save(new Book(null, "testBook", "testType", 10));
		Book bookSaved2 = bookRepository.save(new Book(null, "testBook", "testType", 10));
		assertThat(bookRepository.findAll()).containsExactly(bookSaved1, bookSaved2);
		
		bookRepository.deleteAll();
		
		Collection<Book> bookListAfterDeleteAll = bookRepository.findAll();
		
		assertThat(bookListAfterDeleteAll).doesNotContain(bookSaved1, bookSaved2);
		assertThat(bookListAfterDeleteAll).isEmpty();
		
	}
	
}
