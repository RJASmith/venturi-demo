package com.venturi.demo.controller;

import com.venturi.demo.entity.Book;
import com.venturi.demo.entity.BorrowOutput;
import com.venturi.demo.repository.BookRepository;
import com.venturi.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/booksApi")
public class BorrowController {

    private BookRepository bookRepository;
    private StudentRepository studentRepository;

    final String BORROWED_TEXT = "Book successfully borrowed";
    final String NOT_BORROWED_OTHER_USER_TEXT = "Book is borrowed by another user";
    final String NOT_BORROWED_SAME_USER_TEXT = "Book is already borrowed by this user";
    final String INVALID_STUDENT_ID = "Books not borrowed as provided Student ID is invalid, please use another";
    final String INVALID_BOOK_ID = "Book not borrowed as book ID is not valid";
    final String GENERIC_ERROR = "Error Encountered";


    //Input book ids and student id. The output should return a list of which books are successfully borrowed and
    //which ones failed due to unavailability or any other reason.
    @GetMapping("/borrow")
    public List<BorrowOutput> borrowBooks(
            @RequestParam(value = "bookIds") List<Integer> bookIds,
            @RequestParam(value = "studentId") Integer studentId) {

        Long studentLong = studentId.longValue();

        //check that StudentID is a valid ID. Return early with an exception message if not
        if (studentIsValid(studentLong)) {
            Book invalidIdBook = createExceptionBook(GENERIC_ERROR, studentLong);
            return List.of(new BorrowOutput(invalidIdBook, INVALID_STUDENT_ID));
        }

        //return the books that are attempting to be borrowed
        List<Book> books =  bookIds.stream().map(this::getBookForId).toList();

        //return output object for each book
        return books.stream().map(
                book -> createBorrowOutput(book, studentLong)).toList();
    }

    //Create BorrowOutput for book
    private BorrowOutput createBorrowOutput(Book book, Long studentId) {
        BorrowOutput borrowOutput = new BorrowOutput();
        borrowOutput.setBook(book);

        //Update BorrowResult text depending on if invalid book ID or not
        if (Objects.equals(book.getTitle(), INVALID_BOOK_ID)) {
            borrowOutput.setBorrowResult(INVALID_BOOK_ID);
        } else {
            borrowOutput.setBorrowResult(handleValidBook(book, studentId));
        }

        return borrowOutput;
    }


    //Update book ava
    private String handleValidBook(Book book, Long studentId) {
        if(book.isAvailable()) {
            //Update available book to no longer be available
            updateBookAvailability(book, studentId);
            return BORROWED_TEXT;
        } else {
            //If book is not available, change message depending on who has it borrowed
            return Objects.equals(book.getBorrowerId(), studentId) ?
                    NOT_BORROWED_SAME_USER_TEXT :
                    NOT_BORROWED_OTHER_USER_TEXT;
        }
    }

    //Update a book's availability to no longer be available
    //If I were to add functionality to check books back in, would probably change this to also make them available again.
    private void updateBookAvailability(Book book, Long studentId) {
        book.setAvailable(false);
        book.setBorrowerId(studentId);
        //UPDATE book SET available = false, borrower_id = studentId WHERE book_id = book.getBookId()
        bookRepository.save(book);
    }

    //Check if student exists in the DB
    private boolean studentIsValid(Long studentId) {
        //SELECT TOP 1 * FROM student WHERE student_id = studentId
        return studentRepository.findById(studentId).isEmpty();
    }

    //Return book if found for Id, exception book if not found
    private Book getBookForId(Integer id) {
        //Try to get book for Id
        Long longId = id.longValue();
        //SELECT TOP 1 * FROM book WHERE book_id = longId
        Optional<Book> book = bookRepository.findById(longId);

        return book.isPresent() ?
                book.get() :
                createExceptionBook(INVALID_BOOK_ID, longId);
    }

    //Return a book with exception text as title
    private Book createExceptionBook(String exceptionText, Long Id) {
        Book exceptionBook = new Book();
        exceptionBook.setTitle(exceptionText);
        exceptionBook.setBookId(Id);

        return exceptionBook;
    }


    @Autowired
    public void setStudentRepository(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


}
