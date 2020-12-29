package com.feri.alessandro.attsw.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feri.alessandro.attsw.project.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	Book findByTitle(String string);

	List<Book> findBooksByType(String string);

}
