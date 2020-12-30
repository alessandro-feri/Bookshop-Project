package com.feri.alessandro.attsw.project.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

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
		
		Optional<Book> bookFound = bookRepository.findByTitle("testTitle");
		assertThat(bookFound.get()).isSameAs(bookSaved);
		
	}
	
	@Test
	public void test_findBooksByType() {
		Book testBook1 = new Book(null, "testTitle1", "type1", 10);
		Book testBook2 = new Book(null, "testTitle2", "type2", 10);
		Book testBook3 = new Book(null, "testTitle3", "type1", 10);
		
		entityManager.persistFlushFind(testBook1);
		entityManager.persistFlushFind(testBook2);
		entityManager.persistFlushFind(testBook3);
		
		List<Book> books = bookRepository.findBooksByType("type1");
		
		assertThat(books).containsExactly(testBook1, testBook3);
		assertThat(books).doesNotContain(testBook2);
	}
	
	@Test
	public void test_findByTitleAndPrice() {
		Book testBook1 = new Book(null, "testTitle1", "testType", 10);
		Book testBook2 = new Book(null, "testTitle2", "testType", 15);
		
		entityManager.persistFlushFind(testBook1);
		entityManager.persistFlushFind(testBook2);
		
		Book book = bookRepository.findByTitleAndPrice("testTitle1", 10);
		
		assertThat(book).isEqualTo(testBook1);
		
	}
	
	@Test
	public void test_findByTitleOrType() {
		Book testBook1 = entityManager.persistFlushFind(new Book(null, "testTitle1", "type1", 10));
		Book testBook2 = entityManager.persistFlushFind(new Book(null, "testTitle2", "type1", 10));
		Book testBook3 = entityManager.persistFlushFind(new Book(null, "testTitle3", "type2", 10));
		Book testBook4 = entityManager.persistFlushFind(new Book(null, "testTitle4", "type1", 10));
		
		List<Book> books = bookRepository.findByTitleOrType("testTitle2", "type1");
		
		assertThat(books).containsExactly(testBook1, testBook2, testBook4);
		assertThat(testBook3).isNotIn(books);
	}
	
	@Test
	public void test_findAllBooksWhosePriceIsWithinAnInterval() {
		Book testBook1 = entityManager.persistFlushFind(new Book(null, "testTitle1", "type1", 30));
		Book testBook2 = entityManager.persistFlushFind(new Book(null, "testTitle2", "type1", 15));
		Book testBook3 = entityManager.persistFlushFind(new Book(null, "testTitle3", "type2", 25));
		Book testBook4 = entityManager.persistFlushFind(new Book(null, "testTitle4", "type1", 40));
		
		List<Book> books = bookRepository.findAllBooksWhosePriceIsWithinAnInterval(10, 35);
		
		assertThat(books).containsExactly(testBook1, testBook2, testBook3);
		assertThat(testBook4).isNotIn(books);
	}
	
	
	
}
