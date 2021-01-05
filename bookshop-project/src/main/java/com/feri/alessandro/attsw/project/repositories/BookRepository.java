package com.feri.alessandro.attsw.project.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.feri.alessandro.attsw.project.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	Optional<Book> findBookByTitle(String title);

	@Query("Select b from Book b where b.type = :type")
	List<Book> findBooksByType(@Param("type") String type);
	
	@Query("Select b from Book b where b.title = :title or b.type = :type")
	List<Book> findBooksByTitleOrType(@Param("title") String title, @Param("type") String type);

	@Query("Select b from Book b where b.title = :title and b.price = :price")
	Optional<Book> findBookByTitleAndPrice(@Param("title") String title, @Param("price") int price);

	@Query("Select b from Book b where b.price > :min and b.price < :max")
	List<Book> findAllBooksWhosePriceIsWithinAnInterval(@Param("min") int min, @Param("max") int max);
	
	@Query("Select b from Book b where b.type = :type order by b.title")
	List<Book> findAllBooksByTypeOrderByTitle(@Param("type") String type);
	
}
