INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES  (1, 'First book', 'First Author', 'first_isbn', 1.11, 'First description', 'first_image.img', 0),
        (2, 'Second book', 'Second Author', 'second_isbn', 2.22, 'Second description', 'second_image.img', 0),
        (3, 'Third book', 'Third Author', 'third_isbn', 3.33, 'Third description', 'third_image.img', 0);

INSERT INTO categories (id, name, description, is_deleted)
VALUES  (1, 'first category', 'first category', 0),
        (2, 'second category', 'second category', 0);

INSERT INTO books_categories (book_id, category_id)
VALUES  (1, 1),
        (2, 1),
        (3, 2);