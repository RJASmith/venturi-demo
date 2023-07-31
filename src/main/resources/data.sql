--Initialise books in DB
INSERT INTO book (title, available) VALUES ('The First Book', true), ('The Second Book', true), ('History Book', true)

--Initialise Authors in db
INSERT INTO author (name) VALUES ('Gronald Dempsey'), ('Cloon Maroon'), ('Harvey Malarkey')

--Initialise Book Authors in db
INSERT INTO book_author (book_id, author_id) VALUES (1, 1), (2, 2), (3, 3), (3, 1)

--Initialise Students in db
INSERT INTO student (name) VALUES ('Reilly Smith'), ('Liam Howard')