package com.venturi.demo;

import com.venturi.demo.controller.BorrowController;
import com.venturi.demo.entity.Author;
import com.venturi.demo.entity.Book;
import com.venturi.demo.entity.BorrowOutput;
import com.venturi.demo.entity.Student;
import com.venturi.demo.repository.AuthorRepository;
import com.venturi.demo.repository.BookRepository;
import com.venturi.demo.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;


@SpringBootTest
public class BorrowControllerIntegrationTests {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;
    BorrowController borrowController;

    @BeforeEach
    public void setUp() {
        borrowController = new BorrowController();

        //Setup test student
        Student testStudent = new Student(1L, "Test Student");

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
        studentRepository.save(testStudent);
        bookRepository.save(testBook);
        bookRepository.save(testBookTwo);
        authorRepository.save(testAuthor);

        borrowController.setStudentRepository(studentRepository);
        borrowController.setBookRepository(bookRepository);
    }

    @Test
    void singleBookBorrowedSuccessfully() {
        Integer studentId = 1;
        List<Integer> bookIds = List.of(1);

        List<BorrowOutput> outputs = borrowController.borrowBooks(bookIds, studentId);

        assert !outputs.isEmpty();

        BorrowOutput firstOutput = outputs.get(0);
        assert Objects.equals(firstOutput.getBorrowResult(), "Book successfully borrowed");

    }

    @Test
    void multipleBooksBorrowedSuccessfully() {
        Integer studentId = 1;
        List<Integer> bookIds = List.of(1, 2);

        List<BorrowOutput> outputs = borrowController.borrowBooks(bookIds, studentId);

        assert !outputs.isEmpty();
        assert outputs.size() == 2;

        BorrowOutput firstOutput = outputs.get(0);
        BorrowOutput secondOutput = outputs.get(1);

        assert Objects.equals(firstOutput.getBook().getTitle(), "Test Book");
        assert Objects.equals(firstOutput.getBorrowResult(), "Book successfully borrowed");

        assert Objects.equals(secondOutput.getBook().getTitle(), "Second Book");
        assert Objects.equals(secondOutput.getBorrowResult(), "Book successfully borrowed");
    }

    @Test
    void invalidBookIdFailsBorrowing() {
        Integer studentId = 1;
        List<Integer> bookIds = List.of(10);

        List<BorrowOutput> outputs = borrowController.borrowBooks(bookIds, studentId);

        assert !outputs.isEmpty();

        BorrowOutput firstOutput = outputs.get(0);
        assert Objects.equals(firstOutput.getBorrowResult(), "Book not borrowed as book ID is not valid");
    }

    @Test
    void invalidStudentIdFailsBorrowing() {
        Integer studentId = 10;
        List<Integer> bookIds = List.of(1);

        List<BorrowOutput> outputs = borrowController.borrowBooks(bookIds, studentId);

        assert !outputs.isEmpty();

        BorrowOutput firstOutput = outputs.get(0);
        assert Objects.equals(firstOutput.getBorrowResult(), "Books not borrowed as provided Student ID is invalid, please use another");
    }

    //more test criteria would go below normally
    //Testing when book is already booked out by someone else
    //testing when book is already booked out by same student ID
    //probably some to ensure no NPEs get hit at various stages
}