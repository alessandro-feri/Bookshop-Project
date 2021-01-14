package com.feri.alessandro.attsw.project.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;
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
	public void test_findAllWithEmptyDatabase() {
		List<Book> books = bookRepository.findAll();
		
		assertThat(books).isEmpty();;
	}

	@Test
	public void test_findAllUsingSave() {
		Book bookSaved = bookRepository.save(new Book(null, "testBook", "testType", 10));
		
		List<Book> books = bookRepository.findAll();
		
		assertThat(books).containsExactly(bookSaved);
	}
	
	@Test
	public void test_findBookByTitle() {
		Book bookSaved = new Book(null, "testBook", "testType", 10);
		
		entityManager.persistFlushFind(bookSaved);
		
		Optional<Book> found = bookRepository.findByTitle("testBook");
		
		assertEquals(found.get(), bookSaved);
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
	public void test_findBooksByTitleOrType() {
		Book testBook1 = entityManager.persistFlushFind(new Book(null, "testTitle1", "type1", 10));
		Book testBook2 = entityManager.persistFlushFind(new Book(null, "testTitle2", "type1", 10));
		Book testBook3 = entityManager.persistFlushFind(new Book(null, "testTitle3", "type2", 10));
		Book testBook4 = entityManager.persistFlushFind(new Book(null, "testTitle4", "type1", 10));
		
		List<Book> books = bookRepository.findBooksByTitleOrType("testTitle2", "type1");
		
		assertThat(books).containsExactly(testBook1, testBook2, testBook4);
		assertThat(testBook3).isNotIn(books);
	}
	
	@Test
	public void test_findBookByTitleAndPrice() {
		Book testBook1 = new Book(null, "testTitle1", "testType", 10);
		Book testBook2 = new Book(null, "testTitle2", "testType", 10);
		Book testBook3 = new Book(null, "testTitle1", "testType", 15);

		entityManager.persistFlushFind(testBook1);
		entityManager.persistFlushFind(testBook2);
		entityManager.persistFlushFind(testBook3);

		Optional<Book> book = bookRepository.findBookByTitleAndPrice("testTitle1", 10);

		assertEquals(book.get(), testBook1);		
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
	
	@Test
	public void test_findAllBooksByTypeOrderByTitle() {
		Book testBook1 = entityManager.persistFlushFind(new Book(null, "La Divina Commedia", "type1", 55));
		Book testBook2 = entityManager.persistFlushFind(new Book(null, "Geronimo Stilton", "type2", 15));
		Book testBook3 = entityManager.persistFlushFind(new Book(null, "Il ritratto di Dorian Gray", "type1", 26));
		Book testBook4 = entityManager.persistFlushFind(new Book(null, "Harry Potter e la pietra filosofale", "type1", 30));
		
		List<Book> books = bookRepository.findAllBooksByTypeOrderByTitle("type1");
		
		assertThat(books).containsExactly(testBook4, testBook3, testBook1);
		assertThat(books).doesNotContain(testBook2);		
	}
	
}