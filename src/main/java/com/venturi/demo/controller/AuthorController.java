package com.venturi.demo.controller;

import com.venturi.demo.entity.Author;
import com.venturi.demo.entity.Book;
import com.venturi.demo.repository.AuthorRepository;
import com.venturi.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/authorApi")
public class AuthorController {

    AuthorRepository authorRepository;


    BookRepository bookRepository;

    private final String NO_BOOK_FOUND = "No Books found for Author ID Provided";

    //Implement an api to return available(able to borrow) books from a particular author.
    //Input author id and return a JSON object with a list of books (book id, name, authors)
    @GetMapping("/books")
    public List<Book> returnAuthorBooks(@RequestParam(value = "authorId") Integer authorId) {

        Author author;
        List<Book> books = new ArrayList<>();
        //get the author
        //SELECT * FROM author WHERE authord_id = authorId
        Optional<Author> optionalAuthor = authorRepository.findById(authorId.longValue());

        //If no author returned, return an exception state
        if (optionalAuthor.isPresent()) {
            author = optionalAuthor.get();

            //What would have been used here if not using JPA query and its funky table linking shenanigans
            //SELECT * FROM book b
            //LEFT JOIN book_author ba ON ba.book_id = b.book_id
            //WHERE ba.author_id = author
            books = bookRepository.findByBookAuthors(author);
        } else {
            Book emptyBook = new Book();
            emptyBook.setTitle(NO_BOOK_FOUND);
            books.add(emptyBook);
        }

        return books;
    }

    @Autowired
    public void setAuthorRepository(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

}
