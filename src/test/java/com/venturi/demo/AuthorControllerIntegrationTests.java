package com.venturi.demo;

import com.venturi.demo.controller.AuthorController;
import com.venturi.demo.entity.Author;
import com.venturi.demo.entity.Book;
import com.venturi.demo.repository.AuthorRepository;
import com.venturi.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@SpringBootTest
public class AuthorControllerIntegrationTests {

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookRepository bookRepository;

    AuthorController authorController;

    @BeforeEach
    public void setUp() {
        authorController = new AuthorController();

        //Setup test books
        Book testBook = new Book(1L, "Test Book", true, null, null );
        Book testBookTwo = new Book(2L, "Second Book", true, null, null );


        //Setup test author
        Author testAuthor = new Author(1L, "Test Author", null);

        //Setup book author list and author book list
        Set<Book> bookSet = new HashSet<>();
        bookSet.add(testBook);

        Set<Author> authorSet = new HashSet<>();
        authorSet.add(testAuthor);

        testBook.setBookAuthors(authorSet);
        testAuthor.setAuthorBooks(bookSet);

        //Save to Repos
        bookRepository.save(testBook);
        bookRepository.save(testBookTwo);
        authorRepository.save(testAuthor);

        authorController.setAuthorRepository(authorRepository);
        authorController.setBookRepository(bookRepository);
    }

    //get books for Author provided
    @Test
    void checkBooksForValidAuthor() {
        List<Book> authorBooks = authorController.returnAuthorBooks(1);
        assert !authorBooks.isEmpty();
        assert Objects.equals(authorBooks.get(0).getTitle(), "Test Book");
    }


    //attempt with invalid author ID
    @Test
    void checkBooksForInvalidAuthor() {
        List<Book> authorBooks = authorController.returnAuthorBooks(20);
        assert !authorBooks.isEmpty();
        assert Objects.equals(authorBooks.get(0).getTitle(), "No Books found for Author ID Provided");
    }

}
