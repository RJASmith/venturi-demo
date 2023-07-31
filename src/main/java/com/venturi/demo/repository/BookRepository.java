package com.venturi.demo.repository;

import com.venturi.demo.entity.Author;
import com.venturi.demo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByBookAuthors(Author author);
}
