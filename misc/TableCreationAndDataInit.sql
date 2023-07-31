--Have gotten used to using Sybase over the last couple years, so blame any strange SQL on that ;)

--book
CREATE TABLE book (
    book_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    title VARCHAR(64) NOT NULL,
    available boolean NOT NULL,
    borrower_id BIGINT,
    CONSTRAINT fk_student FOREIGN KEY (borrower_id) REFERENCES student(student_id)
)
--author
CREATE TABLE author (
    author_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(64) NOT NULL
)
--book_author
CREATE TABLE book_author (
    book_id BIGINT NOT NULL PRIMARY KEY,
    author_id BIGINT NOT NULL PRIMARY KEY,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES author(author_id)
    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES book(book_id)
)

--student
CREATE TABLE student (
    student_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(64) NOT NULL
)


--from the data.sql file in the project, run on startup to initialise data
--Initialise books in DB
INSERT INTO book (title, available) VALUES ('The First Book', true), ('The Second Book', true), ('History Book', true)

--Initialise Authors in db
INSERT INTO author (name) VALUES ('Gronald Dempsey'), ('Cloon Maroon'), ('Harvey Malarkey')

--Initialise Book Authors in db
INSERT INTO book_author (book_id, author_id) VALUES (1, 1), (2, 2), (3, 3), (3, 1)

--Initialise Students in db
INSERT INTO student (name) VALUES ('Reilly Smith'), ('Liam Howard')